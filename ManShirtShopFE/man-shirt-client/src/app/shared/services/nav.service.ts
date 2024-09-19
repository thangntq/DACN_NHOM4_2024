import { Injectable, HostListener } from "@angular/core";
import { BehaviorSubject } from "rxjs";
import { HomeService } from "./home.service";
import { discount } from "../classes/product";
import { CategoryService } from "./category.service";

// Menu
export interface Menu {
  path?: string;
  title?: string;
  type?: string;
  megaMenu?: boolean;
  image?: string;
  active?: boolean;
  badge?: boolean;
  badgeText?: string;
  children?: Menu[];
}

@Injectable({
  providedIn: "root",
})
export class NavService {
  constructor(
    private homeService: HomeService,
    private categoryService: CategoryService
  ) {
    this.homeService.findAllDiscount().subscribe((discounts) => {
      const children: any[] = [];
      discounts.forEach((discount) => {
        children.push({
          path: "/shop/sale/" + discount.name.replace(" ", "-"),
          title: discount.name,
          type: "link",
        });
      });
      this.MENUITEMS.push({
        title: "SALE",
        type: "sub",
        active: false,
        children: children,
      });
    });
    categoryService.getCategorys.subscribe({
      next: (res) => {
        console.log(res);
        const children: any[] = [];
        res.forEach((item) => {
          const children1: any[] = [];
          item.category.forEach((value) => {
            children1.push({
              path: "/shop",
              title: value.name,
              type: "link",
            });
          });
          children.push({
            title: item.name,
            type: "sub",
            active: false,
            children: children1,
          });
        });
        this.MENUITEMS.push({
          title: "SẢN PHẨM",
          type: "sub",
          megaMenu: true,
          active: false,
          children: children,
        });
      },
    });
  }

  public screenWidth: any;
  public leftMenuToggle: boolean = false;
  public mainMenuToggle: boolean = false;

  // Windows width
  @HostListener("window:resize", ["$event"])
  onResize(event?) {
    this.screenWidth = window.innerWidth;
  }

  MENUITEMS: Menu[] = [
    {
      path: "/shop/san-pham-moi",
      title: "SẢN PHẨM MỚI",
      type: "link",
    },
    {
      title: "LIÊN HỆ",
      type: "sub",
      active: false,
      children: [
        {
          path: "/shop",
          title: "Ưu đãi & chính sách",
          type: "link",
        },
        {
          path: "/shop",
          title: "Thông tin liên hệ",
          type: "link",
        },
      ],
    },
    {
      path: "/blog",
      title: "BLOGS",
      type: "link",
    },
  ];

  LEFTMENUITEMS: Menu[] = [
    {
      title: "clothing",
      type: "sub",
      megaMenu: true,
      active: false,
      children: [
        {
          title: "mens fashion",
          type: "link",
          active: false,
          children: [
            { path: "/home/fashion", title: "sports wear", type: "link" },
            { path: "/home/fashion", title: "top", type: "link" },
            { path: "/home/fashion", title: "bottom", type: "link" },
            { path: "/home/fashion", title: "ethic wear", type: "link" },
            { path: "/home/fashion", title: "sports wear", type: "link" },
            { path: "/home/fashion", title: "shirts", type: "link" },
            { path: "/home/fashion", title: "bottom", type: "link" },
            { path: "/home/fashion", title: "ethic wear", type: "link" },
            { path: "/home/fashion", title: "sports wear", type: "link" },
          ],
        },
        {
          title: "women fashion",
          type: "link",
          active: false,
          children: [
            { path: "/home/fashion", title: "dresses", type: "link" },
            { path: "/home/fashion", title: "skirts", type: "link" },
            { path: "/home/fashion", title: "westarn wear", type: "link" },
            { path: "/home/fashion", title: "ethic wear", type: "link" },
            { path: "/home/fashion", title: "bottom", type: "link" },
            { path: "/home/fashion", title: "ethic wear", type: "link" },
            { path: "/home/fashion", title: "sports wear", type: "link" },
            { path: "/home/fashion", title: "sports wear", type: "link" },
            { path: "/home/fashion", title: "bottom wear", type: "link" },
          ],
        },
      ],
    },
  ];

  // Array
  items = new BehaviorSubject<Menu[]>(this.MENUITEMS);
  leftMenuItems = new BehaviorSubject<Menu[]>(this.LEFTMENUITEMS);
}
