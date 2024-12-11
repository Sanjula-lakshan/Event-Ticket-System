import {Component, OnInit} from '@angular/core';
import {HttpService} from '../services/HttpService';

@Component({
  selector: 'app-dashboard',
  imports: [],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit{

  allTickets: string = '';
  soldTickets: string = '';
  remainingTickets: string = '';

  constructor(private httpService: HttpService) { }

  ngOnInit(): void {
    this.loadAllTickets();
    this.loadSoldTickets();
    this.loadRemainingTickets();
  }

  loadAllTickets(): void {
   this.httpService.getDashboardData<any>('dashboard/totaltickets').subscribe((data) => {
     this.allTickets = data;
   })
  }

  loadSoldTickets(): void {
    this.httpService.getDashboardData<any>('dashboard/soldtickets').subscribe((data) => {
      this.soldTickets = data;
    })
  }

  loadRemainingTickets(): void {
    this.httpService.getDashboardData<any>('dashboard/remainingtickets').subscribe((data) => {
      this.remainingTickets = data;
    })
  }
}
