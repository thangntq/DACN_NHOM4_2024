import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { NgbModalModule } from "@ng-bootstrap/ng-bootstrap";
import { NgSelectModule } from "@ng-select/ng-select";
import { StatComponent } from "./stat/stat.component";
import { TransactionComponent } from "./transaction/transaction.component";
import lottie from "lottie-web";
import { RouterModule } from "@angular/router";
import { defineElement } from "lord-icon-element";
import { TranexchangeComponent } from "./tranexchange/tranexchange.component";
@NgModule({
  declarations: [StatComponent, TransactionComponent, TranexchangeComponent],
  imports: [
    RouterModule,
    CommonModule,
    NgbModalModule,
    FormsModule,
    ReactiveFormsModule,
    NgSelectModule,
  ],
  exports: [StatComponent, TransactionComponent, TranexchangeComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class WidgetModule {
  constructor() {
    defineElement(lottie.loadAnimation);
  }
}
