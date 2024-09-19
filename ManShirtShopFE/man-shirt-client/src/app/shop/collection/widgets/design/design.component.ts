import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { design } from "src/app/shared/classes/product";
import { DesignService } from "src/app/shared/services/design.service";

@Component({
  selector: "app-design",
  templateUrl: "./design.component.html",
  styleUrls: ["./design.component.scss"],
})
export class DesignComponent implements OnInit {
  @Input() designs: any[] = [];
  public designList: design[] = [];

  @Output() designsFilter: EventEmitter<any> = new EventEmitter<any>();

  public collapse: boolean = false;

  constructor(public designService: DesignService) {
    this.designService.getDesign.subscribe(
      (designs) => (this.designList = designs)
    );
  }

  ngOnInit(): void {}

  get filterbydesign() {
    const uniqueDesigns = [];
    this.designList.filter((design) => {
      uniqueDesigns.push(design.name);
    });
    return uniqueDesigns;
  }

  appliedFilter(event) {
    let index = this.designs.indexOf(event.target.value); // checked and unchecked value
    if (event.target.checked)
      this.designs.push(event.target.value); // push in array cheked value
    else this.designs.splice(index, 1); // removed in array unchecked value

    let designs = this.designs.length
      ? { design: this.designs.join(",") }
      : { design: null };
    this.designsFilter.emit(designs);
  }

  // check if the item are selected
  checked(item) {
    if (this.designs.indexOf(item) != -1) {
      return true;
    }
  }
}
