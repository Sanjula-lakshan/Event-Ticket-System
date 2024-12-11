import { Component, OnInit } from '@angular/core';
import { Vendor } from '../models/vendor';
import { HttpService } from '../services/HttpService';
import { MatFormField, MatInput } from '@angular/material/input';
import { MatButton } from '@angular/material/button';
import { FormsModule } from '@angular/forms';
import { NgForOf } from '@angular/common';

@Component({
  selector: 'app-vendor',
  templateUrl: './vendor.component.html',
  styleUrls: ['./vendor.component.css'],
  imports: [NgForOf, FormsModule, MatInput, MatFormField, MatButton],
  standalone: true
})
export class VendorComponent implements OnInit {
  vendors: Vendor[] = [];
  currentVendor: Vendor = { id: 0, name: '', email: '' };

  constructor(private httpService: HttpService) {}

  ngOnInit(): void {
    this.loadVendors();
  }

  loadVendors(): void {
    this.httpService.getAll<Vendor>('vendors').subscribe((data) => {
      this.vendors = data;
    });
  }

  saveVendor(): void {
    if (this.currentVendor.id) {
      this.httpService
        .update<Vendor>('vendors', this.currentVendor)
        .subscribe(() => this.loadVendors(), ()=> location.reload());
    } else {
      this.httpService
        .create<Vendor>('vendors', this.currentVendor)
        .subscribe(() => this.loadVendors(), () => location.reload());
    }

    this.resetForm();
  }

  editVendor(vendor: Vendor): void {
    this.currentVendor = { ...vendor };
  }

  deleteVendor(id: number): void {
    this.httpService.delete('vendors', id).subscribe(() => {
      this.loadVendors();
    });
  }

  resetForm(): void {
    this.currentVendor = { id: 0, name: '', email: '' };
  }
}
