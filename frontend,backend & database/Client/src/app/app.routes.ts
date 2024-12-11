import { Routes } from '@angular/router';
import { MainwindowComponent } from './mainwindow/mainwindow.component';
import { PurchaseTicketComponent } from './purchase-ticket/purchase-ticket.component';
import { VendorComponent } from './vendor/vendor.component';
import { CustomerComponent } from './customer/customer.component';
import { TicketComponent } from './ticket/ticket.component';
import {DashboardComponent} from './dashboard/dashboard.component';
import {ControlPanelComponent} from './control-panel/control-panel.component';

export const routes: Routes = [
  {
    path: '',
    component: MainwindowComponent,
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'control-panel', component: ControlPanelComponent },
      { path: 'dashboard', component: DashboardComponent },
      { path: 'vendor', component: VendorComponent },
      { path: 'customer', component: CustomerComponent },
      { path: 'ticket', component: TicketComponent },
      { path: 'purchase-ticket', component: PurchaseTicketComponent },
    ],
  },
];
