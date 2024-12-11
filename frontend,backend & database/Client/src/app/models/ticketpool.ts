import {Status} from './status';
import {Vendor} from './vendor';

export class Ticketpool {

  public id!: number;
  public name!: string;
  public count!: number;
  public status!: Status;
  public vendor!: Vendor;
}
