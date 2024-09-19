import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { sleeve } from "src/app/shared/classes/product";
import { SleeveService } from "src/app/shared/services/sleeve.service";

@Component({
  selector: "app-sleeve",
  templateUrl: "./sleeve.component.html",
  styleUrls: ["./sleeve.component.scss"],
})
export class SleeveComponent implements OnInit {
  @Input() sleeves: any[] = [];
  public sleeveList: sleeve[] = [];

  @Output() sleevesFilter: EventEmitter<any> = new EventEmitter<any>();

  public collapse: boolean = false;

  constructor(public sleeveService: SleeveService) {
    this.sleeveService.getSleeve.subscribe(
      (sleeves) => (this.sleeveList = sleeves)
    );
  }

  ngOnInit(): void {}

  get filterBySleeve() {
    const uniqueSleeves = [];
    this.sleeveList.filter((sleeve) => {
      uniqueSleeves.push(sleeve.name);
    });
    return uniqueSleeves;
  }

  appliedFilter(event) {
    let index = this.sleeves.indexOf(event.target.value); // checked and unchecked value
    if (event.target.checked)
      this.sleeves.push(event.target.value); // push in array cheked value
    else this.sleeves.splice(index, 1); // removed in array unchecked value

    let sleeves = this.sleeves.length
      ? { sleeve: this.sleeves.join(",") }
      : { sleeve: null };
    this.sleevesFilter.emit(sleeves);
  }

  // check if the item are selected
  checked(item) {
    if (this.sleeves.indexOf(item) != -1) {
      return true;
    }
  }
}
