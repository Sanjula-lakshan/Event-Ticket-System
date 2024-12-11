import { Component, OnInit } from '@angular/core';
import { Ticketpool } from '../models/ticketpool';
import { Vendor } from '../models/vendor';
import { HttpService } from '../services/HttpService';
import { MatFormField, MatInput } from '@angular/material/input';
import { MatButton } from '@angular/material/button';
import { MatOption, MatSelect } from '@angular/material/select';
import { FormsModule } from '@angular/forms';
import { NgForOf } from '@angular/common';
import {Status} from '../models/status';

@Component({
  selector: 'app-ticket',
  templateUrl: './ticket.component.html',
  styleUrls: ['./ticket.component.css'],
  imports: [NgForOf, FormsModule, MatInput, MatFormField, MatButton, MatSelect, MatOption],
  standalone: true
})
export class TicketComponent implements OnInit {
  tickets: Ticketpool[] = [];
  ticketStatus: Status[] = [];
  vendors: Vendor[] = [];
  currentTicket: Ticketpool = { id: 0, name: '', count: 0, status: { id: 0, name: '' }, vendor: { id: 0, name: '', email: '' } };

  constructor(private httpService: HttpService) {}

  ngOnInit(): void {
    this.loadTickets();
    this.loadVendors();
    this.loadTicketStatus();
  }

  loadTickets(): void {
    this.httpService.getAll<Ticketpool>('tickets').subscribe((data) => {
      this.tickets = data;
    });
  }

  loadTicketStatus() : void {
    this.httpService.getAll<Status>('ticketstatus').subscribe((data) => {
      this.ticketStatus = data;
    });
  }

  loadVendors(): void {
    this.httpService.getAll<Vendor>('vendors').subscribe((data) => {
      this.vendors = data;
    });
  }

  saveTicket(): void {
    if (this.currentTicket.id) {
      this.httpService
        .update<Ticketpool>('tickets', this.currentTicket)
        .subscribe(() => this.loadTickets(), () => location.reload());
    }

    else {
      this.httpService
        .create<Ticketpool>('tickets', this.currentTicket)
        .subscribe(() => this.loadTickets(), () => location.reload());
    }

    this.resetForm();
  }

  editTicket(ticket: Ticketpool): void {
    this.currentTicket = { ...ticket };
  }

  deleteTicket(id: number): void {
    this.httpService.delete('tickets', id).subscribe(() => {
      this.loadTickets();
    });
  }

  resetForm(): void {
    this.currentTicket = { id: 0, name: '',count:0, status: { id: 0, name: '' }, vendor: { id: 0, name: '', email: '' } };
  }
}
