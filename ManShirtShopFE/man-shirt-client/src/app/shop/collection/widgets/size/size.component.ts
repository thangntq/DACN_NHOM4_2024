import { Component, OnInit, Input, Output, EventEmitter } from "@angular/core";
import { size } from "../../../../shared/classes/product";
import { SizeService } from "src/app/shared/services/size.service";

@Component({
  selector: "app-size",
  templateUrl: "./size.component.html",
  styleUrls: ["./size.component.scss"],
})
export class SizeComponent implements OnInit {
  @Input() size: any[] = [];
  public sizeList: size[] = [];
  @Output() sizeFilter: EventEmitter<any> = new EventEmitter<any>();

  public collapse: boolean = false;

  constructor(public sizeService: SizeService) {
    this.sizeService.getSizes.subscribe((size) => (this.sizeList = size));
  }

  ngOnInit(): void {}

  get filterbysize() {
    const uniqueSizes = [];
    this.sizeList.filter((size) => {
      uniqueSizes.push(size.code);
    });
    return uniqueSizes;
  }

  appliedFilter(event) {
    let index = this.size.indexOf(event.target.value); // checked and unchecked value
    if (event.target.checked)
      this.size.push(event.target.value); // push in array cheked value
    else this.size.splice(index, 1); // removed in array unchecked value

    let size = this.size.length
      ? { size: this.size.join(",") }
      : { size: null };
    this.sizeFilter.emit(size);
  }

  // check if the item are selected
  checked(item) {
    if (this.size.indexOf(item) != -1) {
      return true;
    }
  }
}
