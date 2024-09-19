import { Component, OnInit } from "@angular/core";
import { Product, category } from "../../classes/product";
import { ProductService } from "../../services/product.service";
import { CategoryService } from "../../services/category.service";
import { ActivatedRoute } from "@angular/router";

@Component({
  selector: "app-categories",
  templateUrl: "./categories.component.html",
  styleUrls: ["./categories.component.scss"],
})
export class CategoriesComponent implements OnInit {
  public categorys: category[] = [];
  public collapse: boolean = true;

  constructor(
    public categoryService: CategoryService,
    private route: ActivatedRoute
  ) {
    this.categoryService.getCategorys.subscribe(
      (category) => (this.categorys = category)
    );
  }

  ngOnInit(): void {}
  checked(item) {
    const category = this.route.snapshot.queryParamMap.get("category");
    if (category && item.name === category) {
      return true;
    }
  }
}
