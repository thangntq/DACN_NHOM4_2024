import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { UIModule } from "../../shared/ui/ui.module";
import { WidgetModule } from "../../shared/widget/widget.module";
import { Ng5SliderModule } from "ng5-slider";
import { Ng2SearchPipeModule } from "ng2-search-filter";
import {
  NgbNavModule,
  NgbDropdownModule,
  NgbPaginationModule,
  NgbModalModule,
} from "@ng-bootstrap/ng-bootstrap";
import { DropzoneModule } from "ngx-dropzone-wrapper";
import { DROPZONE_CONFIG } from "ngx-dropzone-wrapper";
import { DropzoneConfigInterface } from "ngx-dropzone-wrapper";
import { NgSelectModule } from "@ng-select/ng-select";
import { materialComponent } from "./material/material.component";
import { sleeveComponent } from "./sleeve/sleeve.component";
import { collarComponent } from "./collar/collar.component";
import { colorComponent } from "./color/color.component";
import { designComponent } from "./design/design.component";
import { formComponent } from "./form/form.component";
import { sizeComponent } from "./size/size.component";
import lottie from "lottie-web";
import { defineElement } from "lord-icon-element";

const config: DropzoneConfigInterface = {
  // Change this to your upload POST address:
  url: "https://httpbin.org/post",
  maxFilesize: 100,
};

@NgModule({
  declarations: [
    materialComponent,
    sleeveComponent,
    collarComponent,
    colorComponent,
    designComponent,
    formComponent,
    sizeComponent,
  ],
  imports: [
    CommonModule,
    NgbNavModule,
    NgbModalModule,
    FormsModule,
    Ng2SearchPipeModule,
    NgbDropdownModule,
    DropzoneModule,
    ReactiveFormsModule,
    UIModule,
    WidgetModule,
    Ng5SliderModule,
    NgSelectModule,
    NgbPaginationModule,
  ],
  providers: [
    {
      provide: DROPZONE_CONFIG,
      useValue: config,
    },
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class SpecificationsModule {
  constructor() {
    defineElement(lottie.loadAnimation);
  }
}
