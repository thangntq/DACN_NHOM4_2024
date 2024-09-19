import { Component, OnInit } from "@angular/core";

@Component({
  selector: "app-layout-box",
  templateUrl: "./layout-box.component.html",
  styleUrls: ["./layout-box.component.scss"],
})
export class LayoutBoxComponent implements OnInit {
  public layoutsidebar: boolean = false;

  constructor() {}

  ngOnInit(): void {}

  customizeLayoutDark() {
    document.body.classList.toggle("dark");
  }
}
