package com.ManShirtShop.service.productDetail;
import com.ManShirtShop.common.excel.DropdownItem;
import com.ManShirtShop.common.genCode.GenCode;
import com.ManShirtShop.entities.*;
import com.ManShirtShop.entities.Color;
import com.ManShirtShop.repository.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

@Service
public class ProductDetailExcelService {

    private final ColorRepository colorRepository;
    private final ProductRepository productRepository;
    private  final SizeRepository sizeRepository;
    private final ProductDetailRepository productDetailRepository;

    public ProductDetailExcelService(ColorRepository colorRepository, ProductRepository productRepository, SizeRepository sizeRepository,
                                     ProductDetailRepository productDetailRepository) {
        this.colorRepository = colorRepository;
        this.productRepository = productRepository;
        this.sizeRepository = sizeRepository;
        this.productDetailRepository = productDetailRepository;
    }

    private CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle headerCellStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.RED.getIndex());
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setFillForegroundColor(IndexedColors.GOLD.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        return headerCellStyle;
    }

    // Helper method to create headers for the sheet
    private void createHeaders(Sheet sheet, String[] headers, CellStyle headerCellStyle) {
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerCellStyle);
        }
    }

    // Helper method to auto-fit columns after populating data
    private void autoFitColumns(Sheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    // Helper method to generate file name with date and time
    private String getFileNameWithDateTime(String baseFileName) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String dateTimeSuffix = dateFormat.format(new Date());
        String fileExtension = ".xlsx";
        return baseFileName + "_" + dateTimeSuffix + fileExtension;
    }

    private MultipartFile createMultipartFile(String fileName, ByteArrayOutputStream outputStream) {
        return new MultipartFile() {
            @Override
            public String getName() {
                return fileName;
            }

            @Override
            public String getOriginalFilename() {
                return fileName;
            }

            @Override
            public String getContentType() {
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            }

            @Override
            public boolean isEmpty() {
                return outputStream.size() == 0;
            }

            @Override
            public long getSize() {
                return outputStream.size();
            }

            @Override
            public byte[] getBytes() throws IOException {
                return outputStream.toByteArray();
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(outputStream.toByteArray());
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {
                try (FileOutputStream fileOutputStream = new FileOutputStream(dest)) {
                    fileOutputStream.write(outputStream.toByteArray());
                }
            }
        };
    }

    private Map<String, Integer> getColumnMap(Row headerRow) {
        Map<String, Integer> columnMap = new HashMap<>();
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null) {
                String columnHeader = cell.getStringCellValue();
                columnMap.put(columnHeader, i);
            }
        }
        return columnMap;
    }
    private String getCellValueAsString(Cell cell) {
        if (cell != null) {
            cell.setCellType(CellType.STRING);
            return cell.getStringCellValue();
        }
        return "";
    }

    private double getCellValueAsDouble(Cell cell) {
        if (cell != null) {
            cell.setCellType(CellType.NUMERIC);
            return cell.getNumericCellValue();
        }
        return 0.0;
    }

    private int getCellValueAsInteger(Cell cell) {
        if (cell != null) {
            cell.setCellType(CellType.NUMERIC);
            return (int) cell.getNumericCellValue();
        }
        return 0;
    }


    public MultipartFile generateExcel() {
        try  {
            // Load file mẫu Excel từ thư mục resources
            Resource templateResource = new ClassPathResource("template/ProductDetail_Import_Template_Data.xlsx");
            InputStream fileInputStream = templateResource.getInputStream();

            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet dataPrivateSheet = workbook.getSheet("DataPrivate");

            List<Product> products = productRepository.getAllByStatus();
            List<Color> colors = colorRepository.getAllByStatus();
            List<Size> sizes = sizeRepository.getAllByStatus();


            // Create a map to store dropdown data
            Map<Integer, List<DropdownItem>> dropdownData = new HashMap<>();
            dropdownData.put(0, new ArrayList<>(products));
            dropdownData.put(1, new ArrayList<>(colors));
            dropdownData.put(2, new ArrayList<>(sizes));

            // Populate dropdown columns with data from repositories
            for (Map.Entry<Integer, List<DropdownItem>> entry : dropdownData.entrySet()) {
                int columnIndex = entry.getKey();
                List<DropdownItem> dropdownItems = entry.getValue();

                int rowIndex = 0;
                for (DropdownItem dropdownItem : dropdownItems) {
                    Row row = dataPrivateSheet.getRow(rowIndex++);
                    if (row == null) {
                        row = dataPrivateSheet.createRow(rowIndex - 1);
                    }
                    row.createCell(columnIndex).setCellValue(dropdownItem.getName());
                }
            }

            // Auto-fit columns after populating data

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();

            String fileName = getFileNameWithDateTime("ProductDetail_Import_Template_Data");

            return createMultipartFile(fileName, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public MultipartFile exportDataProduct(Integer status) {
        if (status == null || !(status == 0 || status == 1)) {
            throw new IllegalArgumentException("Không có dữ liệu");
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            String sheetName = status == 0 ? "Product Detail Data Active" : "Product Detail Data Inactive";
            Sheet sheet = workbook.createSheet(sheetName);

            CellStyle headerCellStyle = createHeaderCellStyle(workbook);

            String[] headers = {"Bar_Code", "Tên sản phẩm", "Số lượng", "Màu sắc", "Size"};
            createHeaders(sheet, headers, headerCellStyle);

            List<ProductDetail> productDetails = productDetailRepository.findAllByProductDetailIdAndStatusOrderByCreateTimeDesc(status);

            int rowIndex = 1;
            for (ProductDetail x : productDetails) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(x.getBarCode());
                row.createCell(1).setCellValue(x.getProduct().getName());
                row.createCell(2).setCellValue(x.getQuantity());
                row.createCell(3).setCellValue(x.getColor().getName());
                row.createCell(4).setCellValue(x.getSize().getCode());
            }

            // Apply autoFilter to the header row
            sheet.setAutoFilter(CellRangeAddress.valueOf("A1:E1"));

            // Auto-fit columns after populating data
            autoFitColumns(sheet, headers.length);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();

            String fileName = getFileNameWithDateTime(status == 0 ? "ProductDetail_Export_Active" : "ProductDetail_Export_Inactive");

            return createMultipartFile(fileName, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void importDataFromExcel(MultipartFile file) throws Exception {
        List<Integer> errorRows = new ArrayList<>();
        boolean hasValidRow = false;
        boolean allRowsValid = true;

        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            Map<String, Integer> columnMap = getColumnMap(sheet.getRow(0));
            Map<Integer, String> productCodeMap = new HashMap<>();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row dataRow = sheet.getRow(i);
                if (dataRow != null) {
                    boolean isValidRow = true;
                    for (Map.Entry<String, Integer> entry : columnMap.entrySet()) {
                        String columnName = entry.getKey();
                        int columnIndex = entry.getValue();
                        String cellValue = getCellValueAsString(dataRow.getCell(columnIndex));

                        if (StringUtils.isBlank(cellValue)) {
                            isValidRow = false;
                            break;
                        }

                        if (columnName.equals("Số lượng")) {
                            try {
                                int numericValue = Integer.parseInt(cellValue);
                                if (numericValue <= 0) {
                                    isValidRow = false;
                                    System.out.println(9999);
                                    break;
                                }
                            } catch (NumberFormatException e) {
                                isValidRow = false;
                                break;
                            }
                        }
                    }

                    if (isValidRow) {
                        System.out.println(0);
                        int productDetailQuantity = Integer.parseInt(getCellValueAsString(dataRow.getCell(columnMap.get("Số lượng"))));
                        if (productDetailQuantity <= 0) {
                            isValidRow = false;
                            System.out.println(1);
                            errorRows.add(i);
                        } else {
                            ProductDetail productToUpdate;
                            try {
                                productToUpdate = new ProductDetail();
                                productToUpdate.setProduct(productRepository.findByName(getCellValueAsString(dataRow.getCell(columnMap.get("Tên sản phẩm")))));
                                productToUpdate.setColor(colorRepository.findByName(getCellValueAsString(dataRow.getCell(columnMap.get("Màu sắc")))));
                                productToUpdate.setSize(sizeRepository.findByName(getCellValueAsString(dataRow.getCell(columnMap.get("Size")))));
                                productToUpdate.setCreateTime(Timestamp.from(Instant.now()));
                                productToUpdate.setCreateBy("admin");
                                productToUpdate.setStatus(0);
                                productToUpdate.setQuantity(productDetailQuantity);
                                productToUpdate = productDetailRepository.save(productToUpdate);
                                productToUpdate.setBarCode(createBarCode(productToUpdate.getId()));
                                productCodeMap.put(productToUpdate.getId(), productToUpdate.getBarCode());
                                productDetailRepository.save(productToUpdate);
                                hasValidRow = true;
                                System.out.println(2);
                            } catch (Exception e) {
                                errorRows.add(i);
                                allRowsValid = false;
                                e.printStackTrace();
                                System.out.println(3);
                            }
                        }
                    } else {
                        errorRows.add(i);
                        allRowsValid = false;
                        System.out.println(4);
                    }
                }
            }

            workbook.close();

            if (!hasValidRow) {
                System.out.println(5);
                throw new Exception("Import thất bại: Không có dữ liệu hợp lệ để Import");
            } else if (!allRowsValid) {
                throw new Exception("Import thất bại: Có dữ liệu không hợp lệ");
            } else {
                System.out.println("Import thành công");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Import File thất bại");
        }
    }

    private String createBarCode(Integer id) {// quy tắc:
        // https://vi.wikipedia.org/wiki/EAN-13#:~:text=V%C3%A9%2C%20phi%E1%BA%BFu-,Quy%20t%E1%BA%AFc%20t%C3%ADnh%20s%E1%BB%91%20ki%E1%BB%83m%20tra,8%2C10%2C12)
        final String MaQuocGia = "893";
        final String MaDoanhNghiep = "12345";
        String MaSanPham = null;
        if (id < 10) {
            MaSanPham = "000" + String.valueOf(id);
        } else if (id < 100) {
            MaSanPham = "00" + String.valueOf(id);
        } else if (id < 1000) {
            MaSanPham = "0" + String.valueOf(id);
        } else {
            MaSanPham = String.valueOf(id);
        }
        String ma = MaQuocGia + MaDoanhNghiep + MaSanPham;
        int sum = getCharValue(ma.charAt(0)) + getCharValue(ma.charAt(2)) + getCharValue(ma.charAt(4)) +
                getCharValue(ma.charAt(6)) + getCharValue(ma.charAt(8)) + getCharValue(ma.charAt(10));
        int sum2 = getCharValue(ma.charAt(1)) + getCharValue(ma.charAt(3)) + getCharValue(ma.charAt(5)) +
                getCharValue(ma.charAt(7)) + getCharValue(ma.charAt(9)) + getCharValue(ma.charAt(11));
        sum2 = sum2 * 3;
        sum2 = sum2 + sum;
        int sumfinal = sum2 % 10;
        if (sumfinal != 0) {
            sumfinal = 10 - sumfinal;
        }
        String x = ma + sumfinal;
        System.out.println(x);
        return x;
    }
    private int getCharValue(char a) {
        return Character.getNumericValue(a);
    }


}
