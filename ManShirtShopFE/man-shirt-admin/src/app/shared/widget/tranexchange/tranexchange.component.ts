import { Component, Input, OnInit } from "@angular/core";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { ToastrService } from "ngx-toastr";
import { CONFIRMED, UNCONFIRMED } from "src/app/core/constant/order.constant";
import { OrderService } from "src/app/core/services/order.service";

@Component({
  selector: "app-tranexchange",
  templateUrl: "./tranexchange.component.html",
  styleUrls: ["./tranexchange.component.scss"],
})
export class TranexchangeComponent implements OnInit {
  @Input() returns: any[];
  return: any;
  @Input() onLoadingChanged: (loading: boolean, count?: any) => void;
  constructor(
    private orderService: OrderService,
    private toasrService: ToastrService,
    private modalService: NgbModal
  ) {}

  ngOnInit(): void {
    console.log(this.returns);
  }

  confirm(data: any) {
    this.onLoadingChanged(true);
    this.orderService.updateDangGiaoReturn(data.id).subscribe({
      next: (res) => {
        this.toasrService.info("Xác nhận đơn hàng trả " + data.code);
        this.orderService.findReturnByStatus(UNCONFIRMED).subscribe({
          next: (res) => {
            this.returns = res;
            this.onLoadingChanged(false, res.length);
          },
        });
      },
      error: (err) => {
        this.toasrService.error("Vui lòng thử lại");
        this.onLoadingChanged(false);
      },
    });
  }
  cancel(data: any) {
    this.onLoadingChanged(true);
    this.orderService.updateTuChoiReturn(data.id).subscribe({
      next: (res) => {
        this.toasrService.info("Từ chối đơn hàng trả " + data.code);
        this.orderService.findReturnByStatus(UNCONFIRMED).subscribe({
          next: (res) => {
            this.returns = res;
            this.onLoadingChanged(false, res.length);
          },
        });
      },
      error: (err) => {
        this.toasrService.error("Vui lòng thử lại");
        this.onLoadingChanged(false);
      },
    });
  }
  completed(data: any) {
    this.onLoadingChanged(true);
    console.log(data.id);

    this.orderService.updateSuccessReturn(data.id).subscribe({
      next: (res) => {
        if (res.status == 200) {
          this.toasrService.info(
            "Đơn hàng trả " + data.code + " đã trả về shop"
          );
          this.orderService.findReturnByStatus(CONFIRMED).subscribe({
            next: (res) => {
              this.returns = res;
              this.onLoadingChanged(false, res.length);
            },
          });
        }
        this.onLoadingChanged(false);
      },
      error: (err) => {
        this.toasrService.error("Vui lòng thử lại");
        this.onLoadingChanged(false);
      },
    });
  }
  failed(data: any) {}
  openModal(content: any, data: any) {
    this.onLoadingChanged(true);
    if (data.code) {
      this.orderService.findReturnByCode(data.code).subscribe({
        next: (value) => {
          this.return = value.data;
          console.log(value.data);

          this.modalService.open(content, { size: "lg", centered: true });
          this.onLoadingChanged(false);
        },
        error: (error) => {
          this.onLoadingChanged(false);
        },
      });
    }
  }
  openModalVideo(video: any) {
    this.modalService.open(video, { size: "sm", centered: true });
  }
}
