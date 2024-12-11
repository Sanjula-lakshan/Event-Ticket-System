import {Ticketpool} from './ticketpool';
import {Customer} from './customer';

export class PurchaseTicket {

  public id!: number;
  public ticketpool!: Ticketpool;
  public customer!: Customer;
  public purchasedate!: string;

}

