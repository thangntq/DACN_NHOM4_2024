import { ChartType } from "./dashboard.model";

const emailSentBarChart: ChartType = {
  chart: {
    height: 340,
    type: "bar",
    stacked: true,
    toolbar: {
      show: false,
    },
    zoom: {
      enabled: true,
    },
  },
  plotOptions: {
    bar: {
      horizontal: false,
      columnWidth: "15%",
      endingShape: "rounded",
    },
  },
  dataLabels: {
    enabled: false,
  },
  series: [
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
  ],
  xaxis: {
    categories: [
      "Tháng 1",
      "Tháng 2",
      "Tháng 3",
      "Tháng 4",
      "Tháng 5",
      "Tháng 6",
      "Tháng 7",
      "Tháng 8",
      "Tháng 9",
      "Tháng 10",
      "Tháng 11",
      "Tháng 12",
    ],
  },
  colors: ["#556ee6", "#f1b44c", "#34c38f"],
  legend: {
    position: "bottom",
  },
  fill: {
    opacity: 1,
  },
};
const barChart: ChartType = {
  chart: {
    height: 350,
    type: "bar",
    toolbar: {
      show: false,
    },
  },
  plotOptions: {
    bar: {
      horizontal: true,
    },
  },
  dataLabels: {
    enabled: false,
  },
  series: [
    {
      name: "Sản phẩm",
      data: [],
    },
  ],
  colors: ["#34c38f"],
  labels: ["Series 1", "Series 2", "Series 3", "Series 4", "Series 5"],
  grid: {
    borderColor: "#f1f1f1",
  },
};
const monthlyEarningChart: ChartType = {
  chart: {
    height: 200,
    type: "radialBar",
    offsetY: -10,
  },
  plotOptions: {
    radialBar: {
      startAngle: -135,
      endAngle: 135,
      dataLabels: {
        name: {
          fontSize: "13px",
          color: undefined,
          offsetY: 60,
        },
        value: {
          offsetY: 22,
          fontSize: "16px",
          color: undefined,
          formatter: (val) => {
            return val + "%";
          },
        },
      },
    },
  },
  colors: ["#556ee6"],
  fill: {
    type: "gradient",
    gradient: {
      shade: "dark",
      shadeIntensity: 0.15,
      inverseColors: false,
      opacityFrom: 1,
      opacityTo: 1,
      stops: [0, 50, 65, 91],
    },
  },
  stroke: {
    dashArray: 4,
  },
  series: [67],
  labels: ["Series A"],
};

const statData = [
  {
    icon: "bx bx-copy-alt",
    title: "Orders",
    value: "1,235",
  },
  {
    icon: "bx bx-archive-in",
    title: "Revenue",
    value: "$35, 723",
  },
  {
    icon: "bx bx-purchase-tag-alt",
    title: "Average Price",
    value: "$16.2",
  },
];

const donutChart: ChartType = {
  chart: {
    height: 396,
    type: "donut",
  },
  series: [44, 55, 41, 17, 15],
  legend: {
    show: true,
    position: "bottom",
    horizontalAlign: "center",
    verticalAlign: "middle",
    floating: false,
    fontSize: "14px",
    offsetX: 0,
    offsetY: -10,
  },
  labels: ["Series 1", "Series 2", "Series 3", "Series 4", "Series 5"],
  colors: ["#34c38f", "#556ee6", "#f46a6a", "#50a5f1", "#f1b44c"],
  responsive: [
    {
      breakpoint: 600,
      options: {
        chart: {
          height: 240,
        },
        legend: {
          show: false,
        },
      },
    },
  ],
};
const basicColumChart: ChartType = {
  chart: {
    height: 350,
    type: "bar",
    toolbar: {
      show: false,
    },
  },
  plotOptions: {
    bar: {
      horizontal: false,
      endingShape: "rounded",
      columnWidth: "45%",
    },
  },
  dataLabels: {
    enabled: false,
  },
  stroke: {
    show: true,
    width: 2,
    colors: ["transparent"],
  },
  colors: ["#34c38f", "#556ee6", "#f46a6a"],
  series: [
    {
      name: "Đơn Thành công",
      data: [],
    },
    {
      name: "Đơn huỷ",
      data: [],
    },
    {
      name: "Đơn trả",
      data: [],
    },
  ],
  xaxis: {
    categories: [
      "Tháng 1",
      "Tháng 2",
      "Tháng 3",
      "Tháng 4",
      "Tháng 5",
      "Tháng 6",
      "Tháng 7",
      "Tháng 8",
      "Tháng 9",
      "Tháng 10",
      "Tháng 11",
      "Tháng 12",
    ],
  },
  yaxis: {
    title: {
      text: "",
    },
  },
  fill: {
    opacity: 1,
  },
  grid: {
    borderColor: "#f1f1f1",
  },
  tooltip: {
    y: {
      formatter: (val) => {
        return val;
      },
    },
  },
};

export {
  emailSentBarChart,
  monthlyEarningChart,
  statData,
  barChart,
  donutChart,
  basicColumChart,
};
