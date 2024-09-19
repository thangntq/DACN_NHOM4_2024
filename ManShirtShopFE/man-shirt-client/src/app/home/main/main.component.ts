import { Component, OnInit } from "@angular/core";
import { Product, category } from "src/app/shared/classes/product";
import { CategorySlider, ProductSlider } from "src/app/shared/data/slider";
import { CategoryService } from "src/app/shared/services/category.service";
import { HomeService } from "src/app/shared/services/home.service";
import { ProductService } from "src/app/shared/services/product.service";

@Component({
  selector: "app-main",
  templateUrl: "./main.component.html",
  styleUrls: ["./main.component.scss"],
})
export class MainComponent implements OnInit {
  products: Product[] = [];
  topProducts: Product[] = [];
  topDiscountProduct: Product[] = [];
  categories: category[] = [];
  public productCollections: any[] = [
    { id: 1, name: "Sản phẩm bán chạy" },
    { id: 2, name: "Sản phẩm sale nhiều nhất" },
  ];

  constructor(
    public homeService: HomeService,
    public productService: ProductService,
    private categoryService: CategoryService
  ) {
    this.homeService.getTopNewProduct().subscribe((products) => {
      this.products = products;
    });
    this.homeService.getTopProduct().subscribe((products) => {
      this.topProducts = products;
    });
    this.homeService.getTopDiscountProduct().subscribe((products) => {
      this.topDiscountProduct = products;
    });
    this.categoryService.getCategorys.subscribe((res) => {
      this.categories = res;
    });
  }

  public ProductSliderConfig: any = ProductSlider;
  public CategorySliderConfig: any = CategorySlider;
  public sliders = [
    {
      image: "assets/images/slider/slide2.png",
    },
  ];
  // Collection banner
  public collections = [
    {
      image: "assets/images/slider/slide1.png",
    },
    {
      image: "assets/images/slider/slide1.png",
    },
  ];

  // Blog
  public blog = [
    {
      image: "assets/images/blog/1.webp",
      date: "01/06/2023",
      title: "1000+ mẫu áo đồng phục đi biển sành điệu NỔI BẦN BẬT hè này",
      by: "Nguyễn Thị Ngọc",
    },
    {
      image: "assets/images/blog/1.webp",
      date: "01/06/2023",
      title: "1000+ mẫu áo đồng phục đi biển sành điệu NỔI BẦN BẬT hè này",
      by: "Nguyễn Thị Ngọc",
    },
    {
      image: "assets/images/blog/1.webp",
      date: "01/06/2023",
      title: "1000+ mẫu áo đồng phục đi biển sành điệu NỔI BẦN BẬT hè này",
      by: "Nguyễn Thị Ngọc",
    },
    {
      image: "assets/images/blog/1.webp",
      date: "01/06/2023",
      title: "1000+ mẫu áo đồng phục đi biển sành điệu NỔI BẦN BẬT hè này",
      by: "Nguyễn Thị Ngọc",
    },
  ];

  ngOnInit(): void {}

  // Product Tab collection
  getCollectionProducts(collection) {
    if (collection.id == 1) {
      return this.topProducts;
    } else if (collection.id == 2) {
      return this.topDiscountProduct;
    }
    return [];
  }
}
