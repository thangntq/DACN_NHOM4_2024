package com.ManShirtShop.service.product;

import com.ManShirtShop.common.excel.DropdownItem;
import com.ManShirtShop.common.genCode.GenCode;
import com.ManShirtShop.entities.*;
import com.ManShirtShop.repository.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.cache.annotation.CacheEvict;
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
public class ProductExcelService {
    private final CategoryRepository categoryRepository;
    private final CollarRepository collarRepository;
    private final DesignRepository designRepository;
    private final FormRepository formRepository;
    private final SleeveRepository sleeveRepository;
    private final ProductRepository productRepository;
    private final MaterialRepository materialRepository;

    public ProductExcelService(CategoryRepository categoryRepository, CollarRepository collarRepository, DesignRepository designRepository, FormRepository formRepository, SleeveRepository sleeveRepository, ProductRepository productRepository, MaterialRepository materialRepository) {
        this.categoryRepository = categoryRepository;
        this.collarRepository = collarRepository;
        this.designRepository = designRepository;
        this.formRepository = formRepository;
        this.sleeveRepository = sleeveRepository;
        this.productRepository = productRepository;
        this.materialRepository = materialRepository;
    }

    // Helper method to create a cell style for header row (bold, red text, gold background)
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


//    public MultipartFile exportExcelTemplate() {
//        try (Workbook workbook = new XSSFWorkbook()) {
//            Sheet sheet = workbook.createSheet("Product Template");
//
//            CellStyle headerCellStyle = createHeaderCellStyle(workbook);
//
//            String[] headers = {"Mã sản phẩm", "Tên sản phẩm", "Giá", "Trọng lượng", "Loại", "Cổ áo", "Thiết kế", "Kiểu dáng", "Chất liệu", "Tay áo"};
//            createHeaders(sheet, headers, headerCellStyle);
//
//            // Apply autoFilter to the header row
//            sheet.setAutoFilter(CellRangeAddress.valueOf("A1:J1"));
//
//            // Auto-fit columns after populating data
//            autoFitColumns(sheet, headers.length);
//
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            workbook.write(outputStream);
//            workbook.close();
//
//            String fileName = getFileNameWithDateTime("Product_Import_Template");
//
//            return createMultipartFile(fileName, outputStream);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }

    // Helper method to create MultipartFile
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

    // Export product data based on status (active or inactive)
    public MultipartFile exportDataProduct(Integer status) {
        if (status == null || !(status == 0 || status == 1)) {
            throw new IllegalArgumentException("Không có dữ liệu");
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            String sheetName = status == 0 ? "Product Data Active" : "Product Data Inactive";
            Sheet sheet = workbook.createSheet(sheetName);

            CellStyle headerCellStyle = createHeaderCellStyle(workbook);

            String[] headers = {"Mã sản phẩm", "Tên sản phẩm", "Giá", "Trọng lượng", "Loại", "Cổ áo", "Thiết kế", "Kiểu dáng", "Chất liệu", "Tay áo"};
            createHeaders(sheet, headers, headerCellStyle);

            List<Product> products = productRepository.findAllByProductIdAndStatusOrderByCreateTimeDesc(status);

            int rowIndex = 1;
            for (Product product : products) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(product.getCode());
                row.createCell(1).setCellValue(product.getName());
                row.createCell(2).setCellValue(product.getPrice());
                row.createCell(3).setCellValue(product.getWeight());
                row.createCell(4).setCellValue(product.getCategory().getName());
                row.createCell(5).setCellValue(product.getCollar().getName());
                row.createCell(6).setCellValue(product.getDesign().getName());
                row.createCell(7).setCellValue(product.getForm().getName());
                row.createCell(8).setCellValue(product.getMaterial().getName());
                row.createCell(9).setCellValue(product.getSleeve().getName());
            }

            // Apply autoFilter to the header row
            sheet.setAutoFilter(CellRangeAddress.valueOf("A1:J1"));

            // Auto-fit columns after populating data
            autoFitColumns(sheet, headers.length);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();

            String fileName = getFileNameWithDateTime(status == 0 ? "Product_Export_Active" : "Product_Export_InActive");

            return createMultipartFile(fileName, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    //Ver1.0
    // Import product data from an Excel file
//    public void importDataFromExcel(MultipartFile file) throws Exception {
//        List<Integer> errorRows = new ArrayList<>();
//        boolean hasValidRow = false;
//
//        try (InputStream inputStream = file.getInputStream()) {
//            Workbook workbook = new XSSFWorkbook(inputStream);
//            Sheet sheet = workbook.getSheetAt(0);
//
//            Map<String, Integer> columnMap = getColumnMap(sheet.getRow(0));
//            Map<Integer, String> productCodeMap = new HashMap<>();
//
//            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
//                Row dataRow = sheet.getRow(i);
//                if (dataRow != null) {
//                    boolean isValidRow = true;
//                    for (Map.Entry<String, Integer> entry : columnMap.entrySet()) {
//                        String columnName = entry.getKey();
//                        int columnIndex = entry.getValue();
//                        String cellValue = getCellValueAsString(dataRow.getCell(columnIndex));
//
//                        if (StringUtils.isBlank(cellValue)) {
//                            isValidRow = false;
//                            break;
//                        }
//
//                        // Kiểm tra cột "Giá" và "Trọng lượng" chỉ được nhập số thực
//                        if (columnName.equals("Giá") || columnName.equals("Trọng lượng")) {
//                            try {
//                                double numericValue = Double.parseDouble(cellValue);
//                            } catch (NumberFormatException e) {
//                                isValidRow = false;
//                                break;
//                            }
//                        }
//                    }
//                    if (isValidRow) {
//                        String productCode = null;
//                        String productName = getCellValueAsString(dataRow.getCell(columnMap.get("Tên sản phẩm")));
//                        double productPrice = getCellValueAsDouble(dataRow.getCell(columnMap.get("Giá")));
//                        double productWeight = getCellValueAsDouble(dataRow.getCell(columnMap.get("Trọng lượng")));
//
//                        Product existingProduct = productRepository.findByCode(productCode);
//                        Product productToUpdate;
//
//                        try {
//                            if (existingProduct == null) {
//                                productToUpdate = new Product();
//                                productToUpdate.setName(productName);
//                                productToUpdate.setPrice(productPrice);
//                                productToUpdate.setWeight(productWeight);
//                                productToUpdate.setCategory(categoryRepository.findByName(getCellValueAsString(dataRow.getCell(columnMap.get("Loại")))));
//                                productToUpdate.setCollar(collarRepository.findByName(getCellValueAsString(dataRow.getCell(columnMap.get("Cổ áo")))));
//                                productToUpdate.setDesign(designRepository.findByName(getCellValueAsString(dataRow.getCell(columnMap.get("Thiết kế")))));
//                                productToUpdate.setForm(formRepository.findByName(getCellValueAsString(dataRow.getCell(columnMap.get("Kiểu dáng")))));
//                                productToUpdate.setMaterial(materialRepository.findByName(getCellValueAsString(dataRow.getCell(columnMap.get("Chất liệu")))));
//                                productToUpdate.setSleeve(sleeveRepository.findByName(getCellValueAsString(dataRow.getCell(columnMap.get("Tay áo")))));
//                                productToUpdate.setCreateTime(Timestamp.from(Instant.now()));
//                                productToUpdate.setCreateBy("admin");
//                                productToUpdate.setStatus(0);
//                                productToUpdate = productRepository.save(productToUpdate);
//                                productToUpdate.setCode("SP" + GenCode.codeProduct(productToUpdate.getId()));
//                                productCodeMap.put(productToUpdate.getId(), productToUpdate.getCode());
//                            } else {
//                                productToUpdate = existingProduct;
//                                productToUpdate.setName(productName);
//                                productToUpdate.setPrice(productPrice);
//                                productToUpdate.setWeight(productWeight);
//                                productToUpdate.setCategory(categoryRepository.findByName(getCellValueAsString(dataRow.getCell(columnMap.get("Loại")))));
//                                productToUpdate.setCollar(collarRepository.findByName(getCellValueAsString(dataRow.getCell(columnMap.get("Cổ áo")))));
//                                productToUpdate.setDesign(designRepository.findByName(getCellValueAsString(dataRow.getCell(columnMap.get("Thiết kế")))));
//                                productToUpdate.setForm(formRepository.findByName(getCellValueAsString(dataRow.getCell(columnMap.get("Kiểu dáng")))));
//                                productToUpdate.setMaterial(materialRepository.findByName(getCellValueAsString(dataRow.getCell(columnMap.get("Chất liệu")))));
//                                productToUpdate.setSleeve(sleeveRepository.findByName(getCellValueAsString(dataRow.getCell(columnMap.get("Tay áo")))));
//                            }
//
//                            productRepository.save(productToUpdate);
//                            hasValidRow = true;
//                        } catch (Exception e) {
//                            errorRows.add(i);
//                        }
//                    } else {
//                        errorRows.add(i);
//                    }
//                }
//            }
//
//            workbook.close();
//
//            if (!hasValidRow) {
//                System.out.println("Không có dữ liệu hợp lệ để Import");
//                throw new Exception("Import File thất bại");
//            } else if (!errorRows.isEmpty()) {
//                System.out.println("Có " + errorRows.size() + " dòng dữ liệu không hợp lệ:");
//                for (Integer rowNum : errorRows) {
//                    rowNum+=1;
//                    System.out.println("Dòng " + rowNum +" có dữ liệu không hợp lệ.");
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new Exception("Import File thất bại");
//        }
//    }
//    @CacheEvict(value = "importProductCache", allEntries = true)
    public Map<String, Object> importDataFromExcel(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        List<Integer> errorRows = new ArrayList<>();
        boolean hasValidRow = false;
        boolean allRowsValid = true;
        boolean allRowsValid1 = true;
        try {
            try (InputStream inputStream = file.getInputStream()) {
                Workbook workbook = new XSSFWorkbook(inputStream);
                Sheet sheet = workbook.getSheetAt(0);

                Map<String, Integer> columnMap = getColumnMap(sheet.getRow(0));
                Map<Integer, String> productCodeMap = new HashMap<>();

                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row dataRow = sheet.getRow(i);
                    if (dataRow == null) {
                        allRowsValid1 = false;
                        break;
                    }
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

                            if (columnName.equals("Giá") || columnName.equals("Trọng lượng")) {
                                try {
                                    double numericValue = Double.parseDouble(cellValue);
                                    if (numericValue <= 0) {
                                        isValidRow = false;
                                        break;
                                    }
                                } catch (NumberFormatException e) {
                                    isValidRow = false;
                                    break;
                                }
                            }
                        }

                        if (isValidRow) {
                            String productName = getCellValueAsString(dataRow.getCell(columnMap.get("Tên sản phẩm")));
                            double productPrice = getCellValueAsDouble(dataRow.getCell(columnMap.get("Giá")));
                            double productWeight = getCellValueAsDouble(dataRow.getCell(columnMap.get("Trọng lượng")));
                            if (productPrice <= 0 || productWeight <= 0) {
                                isValidRow = false;
                                errorRows.add(i);
                            } else {
                                Product productToUpdate;

                                try {
                                    productToUpdate = new Product();
                                    productToUpdate.setName(productName);
                                    productToUpdate.setPrice(productPrice);
                                    productToUpdate.setWeight(productWeight);
                                    productToUpdate.setCategory(categoryRepository.findByName(getCellValueAsString(dataRow.getCell(columnMap.get("Loại")))));
                                    productToUpdate.setCollar(collarRepository.findByName(getCellValueAsString(dataRow.getCell(columnMap.get("Cổ áo")))));
                                    productToUpdate.setDesign(designRepository.findByName(getCellValueAsString(dataRow.getCell(columnMap.get("Thiết kế")))));
                                    productToUpdate.setForm(formRepository.findByName(getCellValueAsString(dataRow.getCell(columnMap.get("Kiểu dáng")))));
                                    productToUpdate.setMaterial(materialRepository.findByName(getCellValueAsString(dataRow.getCell(columnMap.get("Chất liệu")))));
                                    productToUpdate.setSleeve(sleeveRepository.findByName(getCellValueAsString(dataRow.getCell(columnMap.get("Tay áo")))));
                                    productToUpdate.setCreateTime(Timestamp.from(Instant.now()));
                                    productToUpdate.setCreateBy("admin");
                                    productToUpdate.setStatus(0);
                                    productToUpdate = productRepository.save(productToUpdate);
                                    productToUpdate.setCode("SP" + GenCode.codeProduct(productToUpdate.getId()));
                                    productCodeMap.put(productToUpdate.getId(), productToUpdate.getCode());

                                    productRepository.save(productToUpdate);
                                    hasValidRow = true;
                                } catch (Exception e) {
                                    errorRows.add(i);
                                    allRowsValid = false;
                                    break;
                                }
                            }
                        } else {
                            errorRows.add(i);
                            allRowsValid = false;
                            break;
                        }
                    }
                }

                workbook.close();

                if (!allRowsValid1) {
                    result.put("status", false);
                    result.put("message", "Import File thất bại - bạn không nhập dữ liệu nào để Import");
                } else if (!allRowsValid || !hasValidRow) {
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("Có tổng cộng ").append(errorRows.size()).append(" dòng lỗi, và lỗi ở những dòng: ");
                    for (Integer errorRow : errorRows) {
                        errorDetails.append(errorRow + 1).append(", ");
                    }
                    errorDetails.delete(errorDetails.length() - 2, errorDetails.length());

                    result.put("status", false);
                    result.put("message", errorDetails.toString());
                } else {
                    result.put("status", true);
                    result.put("message", "Import thành công");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", false);
            result.put("message", "Import File thất bại");
        }

        return result;
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

    public MultipartFile generateExcel() {
        try {
            // Load file mẫu Excel từ thư mục resources
            Resource templateResource = new ClassPathResource("template/Product_Import_Template_Data.xlsx");
            InputStream fileInputStream = templateResource.getInputStream();

            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet dataPrivateSheet = workbook.getSheet("DataPrivate");

            List<Category> categories = categoryRepository.getByIdChaNull();
            List<Collar> collars = collarRepository.getAllByStatus();
            List<Design> designs = designRepository.getAllByStatus();
            List<Form> forms = formRepository.getAllByStatus();
            List<Material> materials = materialRepository.getAllByStatus();
            List<Sleeve> sleeves = sleeveRepository.getAllByStatus();

            // Create a map to store dropdown data
            Map<Integer, List<DropdownItem>> dropdownData = new HashMap<>();
            dropdownData.put(0, new ArrayList<>(categories));
            dropdownData.put(1, new ArrayList<>(collars));
            dropdownData.put(2, new ArrayList<>(designs));
            dropdownData.put(3, new ArrayList<>(forms));
            dropdownData.put(4, new ArrayList<>(materials));
            dropdownData.put(5, new ArrayList<>(sleeves));

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

            String fileName = getFileNameWithDateTime("Product_Import_Template");

            return createMultipartFile(fileName, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
