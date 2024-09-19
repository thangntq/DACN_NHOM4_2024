import { Component, OnInit, Input, Output, EventEmitter } from "@angular/core";
import { color } from "src/app/shared/classes/product";
import { ColorService } from "src/app/shared/services/color.service";

@Component({
  selector: "app-colors",
  templateUrl: "./colors.component.html",
  styleUrls: ["./colors.component.scss"],
})
export class ColorsComponent implements OnInit {
  @Input() colors: any[] = [];
  public colorList: color[] = [];

  @Output() colorsFilter: EventEmitter<any> = new EventEmitter<any>();

  public collapse: boolean = false;

  constructor(public colorService: ColorService) {
    this.colorService.getColors.subscribe(
      (colors) => (this.colorList = colors)
    );
  }

  ngOnInit(): void {}

  get filterbycolor() {
    const uniqueColors = [];
    this.colorList.filter((color) => {
      uniqueColors.push(color.name);
    });
    return uniqueColors;
  }

  appliedFilter(event) {
    let index = this.colors.indexOf(event.target.value); // checked and unchecked value
    if (event.target.checked)
      this.colors.push(event.target.value); // push in array cheked value
    else this.colors.splice(index, 1); // removed in array unchecked value

    let colors = this.colors.length
      ? { color: this.colors.join(",") }
      : { color: null };
    this.colorsFilter.emit(colors);
  }

  // check if the item are selected
  checked(item) {
    if (this.colors.indexOf(item) != -1) {
      return true;
    }
  }
}
