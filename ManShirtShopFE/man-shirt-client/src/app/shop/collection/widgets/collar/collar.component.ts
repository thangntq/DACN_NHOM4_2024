import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { collar } from "src/app/shared/classes/product";
import { CollarService } from "src/app/shared/services/collar.service";

@Component({
  selector: "app-collar",
  templateUrl: "./collar.component.html",
  styleUrls: ["./collar.component.scss"],
})
export class CollarComponent implements OnInit {
  @Input() collars: any[] = [];
  public collarList: collar[] = [];

  @Output() collarsFilter: EventEmitter<any> = new EventEmitter<any>();

  public collapse: boolean = false;

  constructor(public collarService: CollarService) {
    this.collarService.getCollar.subscribe(
      (collars) => (this.collarList = collars)
    );
  }

  ngOnInit(): void {}

  get filterbyCollar() {
    const uniqueCollars = [];
    this.collarList.filter((collar) => {
      uniqueCollars.push(collar.name);
    });
    return uniqueCollars;
  }

  appliedFilter(event) {
    let index = this.collars.indexOf(event.target.value); // checked and unchecked value
    if (event.target.checked)
      this.collars.push(event.target.value); // push in array cheked value
    else this.collars.splice(index, 1); // removed in array unchecked value

    let collars = this.collars.length
      ? { collar: this.collars.join(",") }
      : { collar: null };
    this.collarsFilter.emit(collars);
  }

  // check if the item are selected
  checked(item) {
    if (this.collars.indexOf(item) != -1) {
      return true;
    }
  }
}
