import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {MatFormField} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {MatOption, MatSelect} from '@angular/material/select';
import {NgForOf} from '@angular/common';
import {MatButton} from '@angular/material/button';
import {PurchaseTicket} from '../models/purchaseticket';
import {Status} from '../models/status';
import {Vendor} from '../models/vendor';
import {HttpService} from '../services/HttpService';
import {Customer} from '../models/customer';
import {Ticketpool} from '../models/ticketpool';

@Component({
  selector: 'app-config-panel',
  templateUrl: './purchase-ticket.component.html',
  styleUrls: ['./purchase-ticket.component.css'],
  imports: [
    FormsModule,
    MatFormField,
    MatSelect,
    MatOption,
    NgForOf,
    MatButton
  ],
  standalone: true
})
export class PurchaseTicketComponent implements OnInit{

  tickets: Ticketpool[] = [];
  purchaseTickets: PurchaseTicket[] = [];
  currentTicket: PurchaseTicket = {
    id: 0,
    ticketpool: { id: 0, name: '', count: 0, status: { id: 0, name: '' }, vendor: { id: 0, name: '', email: '' } },
    customer: { id: 0, name: '', email: '' },
    purchasedate: ''
  };
  statuses: Status[] = [];
  vendors: Vendor[] = [];
  customers: Customer[] = [];

  constructor(private httpService: HttpService) {}

  ngOnInit(): void {
    this.loadTickets();
    this.loadStatuses();
    this.loadVendors();
    this.loadCustomers();
  }

  // Load all tickets from the backend
  loadTickets(): void {
    this.httpService.getAll<Ticketpool>('tickets').subscribe(data => {
      this.tickets = data;
    });

    this.httpService.getAll<PurchaseTicket>('ticketpurchase').subscribe(data => {
      this.purchaseTickets = data;
    });
  }

  // Load available statuses for the tickets
  loadStatuses(): void {
    this.httpService.getAll<Status>('ticketstatus').subscribe(data => {
      this.statuses = data;
    });
  }

  // Load available vendors for the tickets
  loadVendors(): void {
    this.httpService.getAll<Vendor>('vendors').subscribe(data => {
      this.vendors = data;
    });
  }

  // Load all customers
  loadCustomers(): void {
    this.httpService.getAll<Customer>('customers').subscribe(data => {
      this.customers = data;
    });
  }

  // Save or update the ticket
  saveTicket(): void {
    if (this.currentTicket.id) {
      // Update ticket
      this.httpService.update<PurchaseTicket>('ticketpurchase', this.currentTicket)
        .subscribe(() => {
          this.loadTickets();
        });
    } else {
      // Save new ticket
      this.httpService.create<PurchaseTicket>('ticketpurchase', this.currentTicket)
        .subscribe(() => {
          this.loadTickets();
        });
    }
    location.reload();
  }


  // Edit an existing ticket
  editTicket(ticket: PurchaseTicket): void {
    this.currentTicket = { ...ticket };
  }

  // Delete a ticket
  deleteTicket(id: number): void {
    this.httpService.delete('ticketpurchase', id).subscribe(() => {
      this.loadTickets();
    });
  }
}

