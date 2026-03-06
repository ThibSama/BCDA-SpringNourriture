package com.bcda.Nourriture.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcda.Nourriture.entity.Recette;
import com.bcda.Nourriture.entity.RecetteIngredient;
import com.bcda.Nourriture.exception.RecetteNotFoundException;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ExportService {

    private final RecetteService recetteService;

    public byte[] exportRecetteToPdf(Long recetteId) throws IOException {
        Recette recette = recetteService.getRecetteById(recetteId)
                .orElseThrow(() -> new RecetteNotFoundException("Recette non trouvée avec l'ID : " + recetteId));

        List<RecetteIngredient> ingredients = recetteService.getRecetteIngredients(recetteId);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PdfWriter writer = new PdfWriter(baos);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            // Titre
            document.add(new Paragraph(recette.getNomPlat())
                    .setFontSize(22)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(10));

            document.add(new Paragraph("Informations générales")
                    .setFontSize(14)
                    .setBold()
                    .setMarginTop(10));

            Table infoTable = new Table(UnitValue.createPercentArray(new float[]{50, 50})).useAllAvailableWidth();
            addInfoRow(infoTable, "Durée de préparation", recette.getDureePreparation() + " min");
            addInfoRow(infoTable, "Durée de cuisson", recette.getDureeCuisson() + " min");
            addInfoRow(infoTable, "Durée totale", (recette.getDureePreparation() + recette.getDureeCuisson()) + " min");
            addInfoRow(infoTable, "Calories", recette.getNombreCalorie() + " kcal");
            addInfoRow(infoTable, "Recette partagée", recette.getPartage() ? "Oui" : "Non");
            if (recette.getUser() != null) {
                addInfoRow(infoTable, "Auteur", recette.getUser().getPrenom() + " " + recette.getUser().getNom());
            }
            document.add(infoTable);

            // Ingrédients
            document.add(new Paragraph("Ingrédients")
                    .setFontSize(14)
                    .setBold()
                    .setMarginTop(16));

            if (ingredients.isEmpty()) {
                document.add(new Paragraph("Aucun ingrédient renseigné.").setItalic());
            } else {
                Table table = new Table(UnitValue.createPercentArray(new float[]{40, 30, 30})).useAllAvailableWidth();

                // En-têtes
                for (String header : new String[]{"Ingrédient", "Quantité", "Unité"}) {
                    table.addHeaderCell(new Cell()
                            .add(new Paragraph(header).setBold())
                            .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                            .setTextAlignment(TextAlignment.CENTER));
                }

                for (RecetteIngredient ri : ingredients) {
                    table.addCell(new Cell().add(new Paragraph(ri.getIngredient().getLibelle())));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(ri.getQuantite())))
                            .setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(ri.getUnite()))
                            .setTextAlignment(TextAlignment.CENTER));
                }
                document.add(table);
            }
        } 
        log.info("PDF généré pour la recette : {}", recette.getNomPlat());
        return baos.toByteArray();
    }

    public byte[] exportRecetteToXlsx(Long recetteId) throws IOException {
        Recette recette = recetteService.getRecetteById(recetteId)
                .orElseThrow(() -> new RecetteNotFoundException("Recette non trouvée avec l'ID : " + recetteId));

        List<RecetteIngredient> ingredients = recetteService.getRecetteIngredients(recetteId);

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet infoSheet = workbook.createSheet("Informations");

            CellStyle titleStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);

            CellStyle headerStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Row titleRow = infoSheet.createRow(0);
            org.apache.poi.ss.usermodel.Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue(recette.getNomPlat());
            titleCell.setCellStyle(titleStyle);
            infoSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));

            String[][] infos = {
                    {"Durée de préparation", recette.getDureePreparation() + " min"},
                    {"Durée de cuisson", recette.getDureeCuisson() + " min"},
                    {"Durée totale", (recette.getDureePreparation() + recette.getDureeCuisson()) + " min"},
                    {"Calories", recette.getNombreCalorie() + " kcal"},
                    {"Recette partagée", recette.getPartage() ? "Oui" : "Non"},
                    {"Auteur", recette.getUser() != null
                            ? recette.getUser().getPrenom() + " " + recette.getUser().getNom()
                            : "Inconnu"}
            };

            for (int i = 0; i < infos.length; i++) {
                Row row = infoSheet.createRow(i + 2);
                org.apache.poi.ss.usermodel.Cell keyCell = row.createCell(0);
                keyCell.setCellValue(infos[i][0]);
                keyCell.setCellStyle(headerStyle);
                row.createCell(1).setCellValue(infos[i][1]);
            }

            infoSheet.autoSizeColumn(0);
            infoSheet.autoSizeColumn(1);

            Sheet ingSheet = workbook.createSheet("Ingrédients");

            Row headerRow = ingSheet.createRow(0);
            String[] headers = {"Ingrédient", "Type", "Quantité", "Unité", "Calories (pour 100g)"};
            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (RecetteIngredient ri : ingredients) {
                Row row = ingSheet.createRow(rowNum++);
                row.createCell(0).setCellValue(ri.getIngredient().getLibelle());
                row.createCell(1).setCellValue(ri.getIngredient().getType().name());
                row.createCell(2).setCellValue(ri.getQuantite());
                row.createCell(3).setCellValue(ri.getUnite());
                row.createCell(4).setCellValue(ri.getIngredient().getNombreCalorie());
            }

            for (int i = 0; i < headers.length; i++) {
                ingSheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            log.info("XLSX généré pour la recette : {}", recette.getNomPlat());
            return baos.toByteArray();
        }
    }

    private void addInfoRow(Table table, String label, String value) {
        table.addCell(new Cell().add(new Paragraph(label).setBold()));
        table.addCell(new Cell().add(new Paragraph(value)));
    }
}
