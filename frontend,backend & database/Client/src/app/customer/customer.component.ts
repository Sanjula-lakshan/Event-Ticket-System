import {Component, Input, OnInit} from '@angular/core';
import { NgForOf } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Customer } from '../models/customer';
import { HttpService } from '../services/HttpService';
import { MatFormField, MatInput } from '@angular/material/input';
import { MatButton } from '@angular/material/button';

@Component({
  selector: 'app-customer',
  templateUrl: './customer.component.html',
  styleUrls: ['./customer.component.css'],
  imports: [
    NgForOf,
    FormsModule,
    MatInput,
    MatFormField,
    MatButton,
  ],
  standalone: true,
})
export class CustomerComponent implements OnInit {
  customers: Customer[] = [];
  currentCustomer: Customer = { id: 0, name: '', email: ''};

  constructor(private httpService: HttpService) {}

  ngOnInit(): void {
    this.loadCustomers();
  }

  // Load all customers
  loadCustomers(): void {
    this.httpService.getAll<Customer>('customers').subscribe((data) => {
      this.customers = data;
    });
  }

  // Save a new customer or update an existing one
  saveCustomer(): void {
    if (this.currentCustomer.id) {
      // Update the customer
      this.httpService
        .update<Customer>('customers', this.currentCustomer)
        .subscribe(() => this.loadCustomers(), () => location.reload());
    } else {
      // Create a new customer
      this.httpService
        .create<Customer>('customers', this.currentCustomer)
        .subscribe(() => this.loadCustomers(), () => location.reload());
    }

    // Reset the form after saving
    this.resetForm();
  }

  // Populate the form for editing
  editCustomer(customer: Customer): void {
    this.currentCustomer = { ...customer };
  }

  // Delete a customer by ID
  deleteCustomer(id: number): void {
    this.httpService.delete('customers', id).subscribe(() => {
      this.loadCustomers();
    });
  }

  // Reset the form fields
  resetForm(): void {
    this.currentCustomer = { id: 0, name: '', email: ''};
  }
}
