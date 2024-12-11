import { Component } from '@angular/core';
import {Vendor} from '../models/vendor';
import {HttpService} from '../services/HttpService';
import {Config} from '../models/config';
import {FormsModule} from '@angular/forms';
import {MatFormField, MatLabel} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {MatOption, MatSelect} from '@angular/material/select';
import {NgForOf} from '@angular/common';
import {MatButton} from '@angular/material/button';
import {MatSnackBar} from '@angular/material/snack-bar';
import {Router} from '@angular/router';

@Component({
  selector: 'app-control-panel',
  imports: [
    FormsModule,
    MatFormField,
    MatInput,
    MatLabel,
    MatSelect,
    MatOption,
    NgForOf,
    MatButton
  ],
  templateUrl: './control-panel.component.html',
  styleUrl: './control-panel.component.css'
})
export class ControlPanelComponent {
  configurations: Config[] = [];
  vendors: Vendor[] = [];
  systemStatstus: string = '';
  currentConfiguration: Config = {
    id: 0,
    ticfrocustomer: 0,
    ticfromvendor: 0,
    remainingtic: 0,
    vendor: {} as Vendor,
  };

  constructor(private httpService: HttpService, private snackBar: MatSnackBar, private router: Router) {}

  ngOnInit(): void {
    this.loadConfigurations();
    this.loadVendors();
  }

  // Load all configurations
  loadConfigurations(): void {
    this.httpService.getAll<Config>('configuration').subscribe(
      (data) => {
        this.configurations = data;
      },
      (error) => {
        console.error('Error fetching configurations:', error);
      }
    );
  }

  // Load all vendors for the dropdown
  loadVendors(): void {
    this.httpService.getAll<Vendor>('vendors').subscribe(
      (data) => {
        this.vendors = data;
      },
      (error) => {
        console.error('Error fetching vendors:', error);
      }
    );
  }

  // Save or update configuration
  saveConfiguration(): void {
    if (this.currentConfiguration.id) {
      // Update configuration
      this.httpService
        .update<Config>('configuration', this.currentConfiguration)
        .subscribe(
          () => {
            this.loadConfigurations();
            this.clearForm();
          },
          (error) => {
            console.error('Error updating configuration:', error);
          }
        );
    } else {
      // Save new configuration
      this.httpService
        .create<Config>('configuration', this.currentConfiguration)
        .subscribe(
          () => {
            this.loadConfigurations();
            this.clearForm();
          },
          (error) => {
            console.error('Error saving configuration:', error);
          }
        );
    }
    location.reload();
  }

  // Edit configuration
  editConfiguration(config: Config): void {
    this.currentConfiguration = { ...config };
  }

  // Delete configuration
  deleteConfiguration(id: number): void {
    this.httpService.delete('configuration', id).subscribe(
      () => {
        alert('Config deleted successfully');
        this.loadConfigurations();
      },
      (error) => {
        console.error('Error deleting configuration:', error);
      }
    );
  }

  // Clear form
  clearForm(): void {
    this.currentConfiguration = {
      id: 0,
      ticfrocustomer: 0,
      ticfromvendor: 0,
      remainingtic: 0,
      vendor: {} as Vendor,
    };
  }

  startSystem(): void {
    this.httpService.get<string>('systemctl/start', { responseType: 'text' as 'json' }).subscribe(
      (data) => {
        this.showSnackBar(data);
        this.router.navigateByUrl("/purchase-ticket").then(() => {
          location.reload();
        });
      },
      (error) => {
        console.error('Error starting the system:', error);
        this.showSnackBar('Error starting the system! Please try again.');
      }
    );
  }

  stopSystem(): void {
    this.httpService.get<string>('systemctl/stop', { responseType: 'text' as 'json' }).subscribe(
      (data) => {
        this.showSnackBar(data);
      },
      (error) => {
        console.error('Error stopping the system:', error);
        this.showSnackBar('Error stopping the system! Please try again.');
      }
    );
  }

  private showSnackBar(message: string): void {
    this.snackBar.open(JSON.parse(message).message, 'Close', {
      duration: 3000,
      verticalPosition: 'top',
      horizontalPosition: 'center',
    });
  }
}
