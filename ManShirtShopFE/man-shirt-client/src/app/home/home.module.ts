import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { SharedModule } from "../shared/shared.module";
import { HomeRoutingModule } from "./home-routing.module";

// Widgest Components
import { SliderComponent } from "./widgets/slider/slider.component";
import { BlogComponent } from "./widgets/blog/blog.component";
import { ServicesComponent } from "./widgets/services/services.component";
import { CollectionComponent } from "./widgets/collection/collection.component";
import { MainComponent } from "./main/main.component";
import { CountdownComponent } from "./widgets/countdown/countdown.component";

@NgModule({
  declarations: [
    // Widgest Components
    SliderComponent,
    BlogComponent,
    ServicesComponent,
    CollectionComponent,
    MainComponent,
    CountdownComponent,
  ],
  imports: [CommonModule, HomeRoutingModule, SharedModule],
})
export class HomeModule {}
