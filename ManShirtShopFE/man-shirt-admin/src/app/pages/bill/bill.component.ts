import { Component, ElementRef, Input, OnInit, ViewChild } from "@angular/core";
import jsPDF from "jspdf";
import html2canvas from "html2canvas";
@Component({
  selector: "app-bill",
  templateUrl: "./bill.component.html",
  styleUrls: ["./bill.component.scss"],
})
export class BillComponent implements OnInit {
  @ViewChild("modalOrder", { static: true }) bill: ElementRef;
  @Input() billData: any;
  constructor() {}

  ngOnInit(): void {
    console.log(this.billData);
  }

  print(): void {
    const doc = new jsPDF();
    const content = this.bill.nativeElement;
    const options = {
      background: "white",
      scale: 3,
    };
    html2canvas(content, options).then((canvas) => {
      const imgData = canvas.toDataURL("image/png");
      const imgWidth = doc.internal.pageSize.getWidth();
      const pageHeight = doc.internal.pageSize.getHeight();
      const imgHeight = (canvas.height * imgWidth) / canvas.width;
      let position = 0;
      doc.addImage(imgData, "PNG", 0, position, imgWidth, imgHeight);
      position -= pageHeight;
      if (position > -canvas.height) {
        doc.addPage();
        doc.addImage(imgData, "PNG", 0, position, imgWidth, imgHeight);
      }
      doc.save("invoice.pdf");
    });
  }

  getTotal1(): number {
    let total: number = 0;
    this.billData.orderDetail.forEach((value) => {
      total += value.unitprice * value.quantity;
    });
    return total;
  }
  getTotalDiscount(): number {
    let total: number = 0;
    this.billData.orderDetail.forEach((value) => {
      if (value?.productDetailId?.product?.productDiscount?.length > 0) {
        total +=
          ((value?.unitprice *
            value?.productDetailId?.product?.productDiscount[0]?.percent) /
            100) *
          value.quantity;
      }
    });
    return total;
  }
  getTotalVoucher(): number {
    let total: number = 0;
    if (this.billData.voucher && this.billData.voucher.type === false) {
      total =
        ((this.getTotal1() - this.getTotalDiscount()) *
          this.billData.voucher.discount) /
        100;
      if (this.billData.voucher.max && total > this.billData.voucher.max) {
        total = this.billData.voucher.max;
      }
    } else if (this.billData.voucher && this.billData.voucher.type === true) {
      total = this.billData.voucher.discount;
    }
    return total;
  }
  getTotal2(): number {
    let total: number = 0;
    total =
      this.getTotal1() -
      this.getTotalDiscount() -
      this.getTotalVoucher() +
      this.billData.freight;
    return total;
  }
}
