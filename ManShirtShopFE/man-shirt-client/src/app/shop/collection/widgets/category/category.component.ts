import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { category } from "src/app/shared/classes/product";
import { CategoryService } from "src/app/shared/services/category.service";

@Component({
  selector: "app-category",
  templateUrl: "./category.component.html",
  styleUrls: ["./category.component.scss"],
})
export class CategoryComponent implements OnInit {
  @Input() category: any[] = [];
  public categoryList: category[] = [];
  @Output() categorysFilter: EventEmitter<any> = new EventEmitter<any>();
  public collapse: boolean = true;
  constructor(public categoryService: CategoryService) {
    this.categoryService.getCategorys.subscribe((category) => {
      this.categoryList = category;
    });
  }
  get filterbycategory() {
    const uniqueCategorys = [];
    this.categoryList.filter((category) => {
      uniqueCategorys.push(category.name);
    });
    return uniqueCategorys;
  }
  appliedFilter(event) {
    let index = this.category.indexOf(event.target.value);
    if (event.target.checked) this.category.push(event.target.value);
    else this.category.splice(index, 1);

    let category = this.category.length
      ? { category: this.category.join(",") }
      : { category: null };
    this.categorysFilter.emit(category);
  }
  ngOnInit(): void {}

  checked(item) {
    if (this.categoryList.indexOf(item) != -1) {
      return true;
    }
  }
}
