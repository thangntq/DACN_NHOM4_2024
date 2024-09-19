import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { ViewportScroller } from "@angular/common";
import { ProductService } from "../../../shared/services/product.service";
import { Product } from "../../../shared/classes/product";
import { ToastrService } from "ngx-toastr";

@Component({
  selector: "app-collection-left-sidebar",
  templateUrl: "./collection-left-sidebar.component.html",
  styleUrls: ["./collection-left-sidebar.component.scss"],
})
export class CollectionLeftSidebarComponent implements OnInit {
  public grid: string = "col-xl-3 col-lg-4 col-md-6 col-6";
  public layoutView: string = "grid-view";
  public products: Product[] = [];
  public colors: any[] = [];
  public size: any[] = [];
  public designs: any[] = [];
  public forms: any[] = [];
  public collars: any[] = [];
  public materials: any[] = [];
  public sleeves: any[] = [];
  public minPrice: number = 0;
  public maxPrice: number = 5000000;
  public tags: any[] = [];
  public category: any[] = [];
  isNotProduct: boolean;
  discount: any;
  public filter: any = {
    color: [],
    oriCategory: [],
    category: [],
    design: [],
    form: [],
    material: [],
    size: [],
    collar: [],
    sleeve: [],
    low: 0,
    high: 0,
    discount: 0,
    idDiscount: 0,
    fillter: 0,
  };
  public pageNo: number = 1;
  public paginate: any;
  public sortBy: string; // Sorting Order
  public mobileSidebar: boolean = false;
  public loader: boolean = true;
  currentPath: any = 0;
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private viewScroller: ViewportScroller,
    public productService: ProductService,
    private toastrService: ToastrService
  ) {
    const currentPath = this.route.snapshot.url.join("/");
    if (currentPath === "san-pham-moi") {
      this.currentPath = 2;
    } else if (currentPath === "giam-gia-sieu-lon") {
      this.currentPath = 1;
    } else if (currentPath === "ban-chay-nhat") {
      this.currentPath = 3;
    }
    this.route.data.subscribe({
      next: (value) => {
        this.discount = value.data;
        this.route.queryParams.subscribe((params) => {
          this.category = params.category ? params.category.split(",") : [];
          this.colors = params.color ? params.color.split(",") : [];
          this.size = params.size ? params.size.split(",") : [];
          this.designs = params.design ? params.design.split(",") : [];
          this.materials = params.material ? params.material.split(",") : [];
          this.forms = params.form ? params.form.split(",") : [];
          this.collars = params.collar ? params.collar.split(",") : [];
          this.sleeves = params.sleeve ? params.sleeve.split(",") : [];
          this.minPrice = params.minPrice ? params.minPrice : this.minPrice;
          this.maxPrice = params.maxPrice ? params.maxPrice : this.maxPrice;
          this.tags = [
            ...this.category,
            ...this.colors,
            ...this.size,
            ...this.designs,
            ...this.materials,
            ...this.forms,
            ...this.collars,
            ...this.sleeves,
          ]; // All Tags Array
          this.filter = {};
          this.filter = {
            color: this.colors,
            size: this.size,
            design: this.designs,
            material: this.materials,
            form: this.forms,
            collar: this.collars,
            sleeve: this.sleeves,
            low: 0,
            high: 0,
            discount: 0,
            oriCategory: this.category ? this.category : [],
            category: [],
            idDiscount: this.discount ? this.discount.id : 0,
            fillter: this.currentPath,
          };
          console.log(this.filter);

          this.sortBy = params.sortBy ? params.sortBy : "ascending";
          this.pageNo = params.page ? params.page : this.pageNo;
          this.productService.getFilter(this.filter).subscribe({
            next: (value) => {
              if (value.length == 0) {
                this.isNotProduct = true;
              } else {
                this.isNotProduct = false;
              }
              this.products = this.productService.sortProducts(
                value,
                this.sortBy
              );
              // if (params.category)
              // this.products = this.products.filter(
              //   (item) => item.category == this.category
              // );
              this.products = this.products.filter(
                (item) =>
                  item.price >= this.minPrice && item.price <= this.maxPrice
              );
              this.paginate = this.productService.getPager(
                this.products.length,
                +this.pageNo
              ); // get paginate object from service
              this.products = this.products.slice(
                this.paginate.startIndex,
                this.paginate.endIndex + 1
              );
            },
            error: (message) => {
              this.toastrService.error("Lỗi khi lấy danh sách sản phẩm");
            },
          });
        });
      },
    });
  }

  ngOnInit(): void {}

  // Append filter value to Url
  updateFilter(tags: any) {
    tags.page = null; // Reset Pagination
    this.router
      .navigate([], {
        relativeTo: this.route,
        queryParams: tags,
        queryParamsHandling: "merge", // preserve the existing query params in the route
        skipLocationChange: false, // do trigger navigation
      })
      .finally(() => {
        this.viewScroller.setOffset([120, 120]);
        // this.viewScroller.scrollToAnchor("products"); // Anchore Link
      });
  }

  // SortBy Filter
  sortByFilter(value) {
    this.router
      .navigate([], {
        relativeTo: this.route,
        queryParams: { sortBy: value ? value : null },
        queryParamsHandling: "merge", // preserve the existing query params in the route
        skipLocationChange: false, // do trigger navigation
      })
      .finally(() => {
        this.viewScroller.setOffset([120, 120]);
        // this.viewScroller.scrollToAnchor("products"); // Anchore Link
      });
  }

  // Remove Tag
  removeTag(tag) {
    this.category = this.category.filter((val) => val !== tag);
    this.colors = this.colors.filter((val) => val !== tag);
    this.size = this.size.filter((val) => val !== tag);
    this.designs = this.designs.filter((val) => val !== tag);
    this.materials = this.materials.filter((val) => val !== tag);
    this.forms = this.forms.filter((val) => val !== tag);
    this.collars = this.collars.filter((val) => val !== tag);
    this.sleeves = this.sleeves.filter((val) => val !== tag);
    let params = {
      category: this.category.length ? this.category.join(",") : null,
      color: this.colors.length ? this.colors.join(",") : null,
      size: this.size.length ? this.size.join(",") : null,
      design: this.designs.length ? this.designs.join(",") : null,
      material: this.materials.length ? this.materials.join(",") : null,
      form: this.forms.length ? this.forms.join(",") : null,
      collar: this.collars.length ? this.collars.join(",") : null,
      sleeve: this.sleeves.length ? this.sleeves.join(",") : null,
    };

    this.router
      .navigate([], {
        relativeTo: this.route,
        queryParams: params,
        queryParamsHandling: "merge", // preserve the existing query params in the route
        skipLocationChange: false, // do trigger navigation
      })
      .finally(() => {
        this.viewScroller.setOffset([120, 120]);
        // this.viewScroller.scrollToAnchor("products"); // Anchore Link
      });
  }

  // Clear Tags
  removeAllTags() {
    this.router
      .navigate([], {
        relativeTo: this.route,
        queryParams: {},
        skipLocationChange: false, // do trigger navigation
      })
      .finally(() => {
        this.viewScroller.setOffset([120, 120]);
        // this.viewScroller.scrollToAnchor("products"); // Anchore Link
      });
  }

  // product Pagination
  setPage(page: number) {
    this.router
      .navigate([], {
        relativeTo: this.route,
        queryParams: { page: page },
        queryParamsHandling: "merge", // preserve the existing query params in the route
        skipLocationChange: false, // do trigger navigation
      })
      .finally(() => {
        this.viewScroller.setOffset([120, 120]);
        // this.viewScroller.scrollToAnchor("products"); // Anchore Link
      });
  }

  // Change Grid Layout
  updateGridLayout(value: string) {
    this.grid = value;
  }

  // Change Layout View
  updateLayoutView(value: string) {
    this.layoutView = value;
    if (value == "list-view") this.grid = "col-lg-12";
    else this.grid = "col-xl-2 col-lg-3 col-md-4 col-6";
  }

  // Mobile sidebar
  toggleMobileSidebar() {
    this.mobileSidebar = !this.mobileSidebar;
  }
}
