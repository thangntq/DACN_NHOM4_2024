import { Component, OnInit, ViewChild } from "@angular/core";

import {
  emailSentBarChart,
  monthlyEarningChart,
  barChart,
  donutChart,
  basicColumChart,
} from "./data";
import { BaseChartDirective } from "ng2-charts";

import { FormBuilder, Validators, FormGroup } from "@angular/forms";
import { NgbModal, NgbModalRef } from "@ng-bootstrap/ng-bootstrap";
import { ChartType } from "./dashboard.model";
import {
  statictical,
  topCustomer,
  topProduct,
} from "src/app/core/models/statictical.models";
import { staticticalService } from "src/app/core/services/statictical.service";
import { EventService } from "../../../core/services/event.service";
import Swal from "sweetalert2";
import { ConfigService } from "../../../core/services/config.service";
import { element } from "protractor";
import { serialMap } from "chartist";

@Component({
  selector: "app-default",
  templateUrl: "./default.component.html",
  styleUrls: ["./default.component.scss"],
})
export class DefaultComponent implements OnInit {
  isVisible: string;
  years: number[] = [2026, 2025, 2024, 2023, 2022, 2021, 2020];
  selectedYear = 2023;
  statData: any[] = [
    {
      icon: "bx bx-dollar-circle",
      title: "Doanh thu",
      value: "150.000.000 VNĐ",
    },
    {
      icon: "bx bx-copy-alt",
      title: "Tổng đơn hàng thành công",
      value: "120",
    },
    {
      icon: "bx bx-cube-alt",
      title: "Số lượng sản phẩm đã bán",
      value: "1500",
    },
    {
      icon: "bx bx-support",
      title: "Đơn hàng đang chờ xử lý",
      value: "15",
    },
  ];

  emailSentBarChart: ChartType;
  basicColumChart: ChartType;
  monthlyEarningChart: ChartType;
  barChart: ChartType;
  donutChart: ChartType;

  transactions: Array<[]>;
  submitted = false;
  formData: FormGroup;
  modalRef: NgbModalRef;
  isActive: string;
  topProduct: topProduct = {
    fullName: null,
    quantity: 0,
  };
  Statictical: statictical = {
    totalByDay: 0,
    totalByMonth: 0,
    totalByYear: 0,
    orderInProgress: 0,
  };

  getAllTotalData: statictical;
  getTopProduct: topProduct[];

  @ViewChild("content") content;

  constructor(
    private modalService: NgbModal,
    private formBuilder: FormBuilder,
    private configService: ConfigService,
    private eventService: EventService,
    private staticticalService: staticticalService
  ) {}

  ngOnInit() {
    this.formData = this.formBuilder.group({
      year: ["", [Validators.required]],
    });
    const attribute = document.body.getAttribute("data-layout");
    this.isVisible = attribute;
    const vertical = document.getElementById("layout-vertical");
    if (vertical != null) {
      vertical.setAttribute("checked", "true");
    }
    if (attribute == "horizontal") {
      const horizontal = document.getElementById("layout-horizontal");
      if (horizontal != null) {
        horizontal.setAttribute("checked", "true");
        console.log(horizontal);
      }
    }

    window.dispatchEvent(new Event("resize"));
    this.loadData();
    this.loadDataTopProduct();
    this.fetchData();
    this.getTopCustomer();
    this.getTotalbyYear();
    this.getOrderByYear();
  }

  loadData(): void {
    this.staticticalService.getAllTotal().subscribe((data) => {
      this.getAllTotalData = data;
      this.statData[0].value = data.totalByDay.toLocaleString("vi-VN") + " VNĐ";
      this.statData[1].value = data.totalByMonth;
      this.statData[2].value = data.totalByYear;
      this.statData[3].value = data.orderInProgress;
      console.log(this.getAllTotalData);
    });
  }
  getTopProduct1: any = [];
  loadDataTopProduct(): void {
    this.staticticalService.getTopProduct().subscribe({
      next: (data) => {
        console.log(data);
        this.donutChart.series = [];
        this.donutChart.labels = [];
        data.forEach((element) => {
          this.donutChart.series.push(element.quantity);
          this.donutChart.labels.push(element.fullName);
        });
      },
    });
  }

  getTopCustomer(): void {
    this.staticticalService.getTopCustomer().subscribe({
      next: (res) => {
        console.log(res);
        this.barChart.series = [
          {
            name: "Sản phẩm",
            data: [],
          },
        ];
        this.barChart.labels = [];
        res.forEach((element) => {
          this.barChart.labels.push(element.fullName);
          this.barChart.series.forEach((series) => {
            if (series.name === "Sản phẩm") {
              series.data.push(element.countProduct);
            }
          });
        });

        console.log(this.barChart);
      },
    });
  }

  getOrderByYear() {
    this.staticticalService.findOrderByMonth(this.selectedYear).subscribe({
      next: (res) => {
        this.basicColumChart.series = [
          {
            name: "Đơn thành công",
            data: [],
          },
          {
            name: "Đơn huỷ",
            data: [],
          },
          {
            name: "Đơn trả Thành công",
            data: [],
          },
        ];
        res.forEach((element) => {
          this.basicColumChart.series.forEach((series) => {
            if (series.name === "Đơn thành công") {
              series.data.push(element.orderSuccess);
            }
            if (series.name === "Đơn huỷ") {
              series.data.push(element.orderCancel);
            }
            if (series.name === "Đơn trả Thành công") {
              series.data.push(element.returnSuccess);
            }
          });
        });
      },
      error: (err) => {},
    });
  }

  getTotalbyYear() {
    this.staticticalService.findTotalByMonth(this.selectedYear).subscribe({
      next: (res) => {
        console.log(res);
        this.emailSentBarChart.series = [
          {
            name: "Doanh thu",
            data: [],
          },
          {
            name: "Số lượng sản phẩm đã bán",
            data: [],
          },
          {
            name: "Đơn hàng",
            data: [],
          },
        ];
        res.forEach((element) => {
          this.emailSentBarChart.series.forEach((series) => {
            if (series.name === "Doanh thu") {
              series.data.push(element.total);
            }
            if (series.name === "Số lượng sản phẩm đã bán") {
              series.data.push(element.productCount);
            }
            if (series.name === "Đơn hàng") {
              series.data.push(element.orderSucsses);
            }
          });
        });
      },
      error: (err) => {},
    });
  }

  /**
   * Fetches the data
   */
  private fetchData() {
    this.emailSentBarChart = emailSentBarChart;
    this.monthlyEarningChart = monthlyEarningChart;
    this.donutChart = donutChart;
    this.basicColumChart = basicColumChart;
    this.barChart = barChart;
    this.isActive = "year";
    this.configService.getConfig().subscribe((data) => {
      this.transactions = data.transactions;
      
    });
  }
  closeModal(): void {
    this.modalRef.close();
  }
  openModal() {
    this.modalService.open(this.content, { centered: true });
  }
  get form() {
    return this.formData.controls;
  }

  /**
   * Change the layout onclick
   * @param layout Change the layout
   */
  changeLayout(layout: string) {
    this.eventService.broadcast("changeLayout", layout);
  }
}
