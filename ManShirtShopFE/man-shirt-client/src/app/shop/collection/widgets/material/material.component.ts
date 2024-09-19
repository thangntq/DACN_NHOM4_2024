import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { material } from "src/app/shared/classes/product";
import { MaterialService } from "src/app/shared/services/material.service";

@Component({
  selector: "app-material",
  templateUrl: "./material.component.html",
  styleUrls: ["./material.component.scss"],
})
export class MaterialComponent implements OnInit {
  @Input() materials: any[] = [];
  public materialList: material[] = [];

  @Output() materialsFilter: EventEmitter<any> = new EventEmitter<any>();

  public collapse: boolean = false;

  constructor(public materialService: MaterialService) {
    this.materialService.getMaterial.subscribe(
      (materials) => (this.materialList = materials)
    );
  }

  ngOnInit(): void {}

  get filterByMaterial() {
    const uniqueMaterials = [];
    this.materialList.filter((material) => {
      uniqueMaterials.push(material.name);
    });
    return uniqueMaterials;
  }

  appliedFilter(event) {
    let index = this.materials.indexOf(event.target.value); // checked and unchecked value
    if (event.target.checked)
      this.materials.push(event.target.value); // push in array cheked value
    else this.materials.splice(index, 1); // removed in array unchecked value

    let materials = this.materials.length
      ? { material: this.materials.join(",") }
      : { material: null };
    this.materialsFilter.emit(materials);
  }

  // check if the item are selected
  checked(item) {
    if (this.materials.indexOf(item) != -1) {
      return true;
    }
  }
}
