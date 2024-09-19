import { Component, OnInit } from "@angular/core";
import { Product } from "src/app/shared/classes/product";
import { ProductService } from "src/app/shared/services/product.service";
import { AuthenticationService } from "../login/auth.service";

@Component({
  selector: "app-history",
  templateUrl: "./history.component.html",
  styleUrls: ["./history.component.scss"],
})
export class HistoryComponent implements OnInit {
  public openDashboard: boolean = false;
  public products: Product[] = [];
  userInfo: any;
  constructor(
    public productService: ProductService,
    private authService: AuthenticationService
  ) {
    this.productService.historyItems.subscribe(
      (response) => (this.products = response)
    );
  }

  ngOnInit(): void {
    this.authService.getUser().subscribe({
      next: (value) => {
        this.userInfo = value[0];
      },
      error: (error) => {},
    });
  }

  ToggleDashboard() {
    this.openDashboard = !this.openDashboard;
  }
}
