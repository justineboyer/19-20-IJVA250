package com.example.demo.service;

import com.example.demo.entity.Client;
import com.example.demo.entity.Facture;
import com.example.demo.entity.LigneFacture;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfImage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
public class ExportService {
    private ClientService clientService;
    private FactureService factureService;

    public ExportService(ClientService clientService, FactureService factureService) {
        this.clientService = clientService;
        this.factureService = factureService;
    }

    public void clientsCSV(Writer writer) throws IOException {
        PrintWriter printWriter = new PrintWriter(writer);

        List<Client> allClients = clientService.findAllClients();
        LocalDate now = LocalDate.now();
        printWriter.println("Id" + ";" + "Nom" + ";" + "Prenom" + ";" + "Date de Naissance");

        for (Client client : allClients) {
            printWriter.println(client.getId() + ";"
                    + client.getNom() + ";"
                    + client.getPrenom() + ";"
                    + client.getDateNaissance().format(DateTimeFormatter.ofPattern("dd/MM/YYYY")));
        }
    }


    public void exportPDF(Long idFacture, OutputStream outputSteam) throws DocumentException, IOException {
        Facture facture = factureService.findById(idFacture);

        // import com.itextpdf.text.*;
        Document document = new Document();
        PdfWriter.getInstance(document, outputSteam);
        document.open();

        PdfPTable table = new PdfPTable(3);
        table.addCell(new PdfPCell(new Phrase("Articles")));
        table.addCell(new PdfPCell(new Phrase("Quantité")));
        table.addCell(new PdfPCell(new Phrase("PrixUnitaire")));
        for (LigneFacture ligneFacture : facture.getLigneFactures()) {
            table.addCell(new PdfPCell(new Phrase(ligneFacture.getArticle().getLibelle())));
            table.addCell(new PdfPCell(new Phrase(ligneFacture.getQuantite())));
            table.addCell(new PdfPCell(new Phrase("" + ligneFacture.getArticle().getPrix())));
        }
        PdfPCell total = new PdfPCell(new Phrase("TOTAL"));
        total.setColspan(2);
        table.addCell(total);

        table.addCell(new PdfPCell(new Phrase("" + facture.getTotal())));


        document.add(table);
        document.close();
    }

    public void feuilleClient(Workbook workbook, Client client) {

        Sheet feuille;
        Row ligne;
        Cell celluleIntitule;
        Cell celluleValeur;

        feuille = workbook.createSheet(client.getNom() + " " + client.getPrenom());

        ligne = feuille.createRow(0);
        celluleIntitule = ligne.createCell(0);
        celluleValeur = ligne.createCell(1);
        celluleIntitule.setCellValue("Nom :");
        celluleValeur.setCellValue(client.getNom());

        ligne = feuille.createRow(1);
        celluleIntitule = ligne.createCell(0);
        celluleValeur = ligne.createCell(1);
        celluleIntitule.setCellValue("Prénom :");
        celluleValeur.setCellValue(client.getPrenom());

        ligne = feuille.createRow(2);
        celluleIntitule = ligne.createCell(0);
        celluleValeur = ligne.createCell(1);
        celluleIntitule.setCellValue("Date de naissance :");
        celluleValeur.setCellValue(client.getDateNaissance().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        ligne = feuille.createRow(3);
        celluleIntitule = ligne.createCell(0);
        celluleValeur = ligne.createCell(1);
        celluleIntitule.setCellValue("Age :");
        celluleValeur.setCellValue(client.getAge());

    }


    public void feuilleFacture(Workbook workbook, Facture facture) {

        Sheet feuille;
        Row ligne;
        Cell celluleIntitule;
        Cell celluleValeur;

        feuille = workbook.createSheet("Facture" + facture.getId());

        ligne = feuille.createRow(0);
        celluleIntitule = ligne.createCell(0);
        celluleIntitule.setCellValue("ID Article");
        celluleIntitule = ligne.createCell(1);
        celluleIntitule.setCellValue("Libelle");
        celluleIntitule = ligne.createCell(2);
        celluleIntitule.setCellValue("Prix Unitaire");
        celluleIntitule = ligne.createCell(3);
        celluleIntitule.setCellValue("Quantite");
        celluleIntitule = ligne.createCell(4);
        celluleIntitule.setCellValue("Prix Total");


        for (LigneFacture ligneFacture : facture.getLigneFactures()) {
            ligne = feuille.createRow(ligne.getRowNum() + 1);
            celluleValeur = ligne.createCell(0);
            celluleValeur.setCellValue(ligneFacture.getArticle().getId());
            celluleValeur = ligne.createCell(1);
            celluleValeur.setCellValue(ligneFacture.getArticle().getLibelle());
            celluleValeur = ligne.createCell(2);
            celluleValeur.setCellValue(ligneFacture.getArticle().getPrix());
            celluleValeur = ligne.createCell(3);
            celluleValeur.setCellValue(ligneFacture.getQuantite());
            celluleValeur = ligne.createCell(4);
            celluleValeur.setCellValue(ligneFacture.getSousTotal());
        }

        ligne = feuille.createRow(ligne.getRowNum() + 1);
        celluleIntitule = ligne.createCell(3);
        celluleIntitule.setCellValue("Total :");
        celluleValeur = ligne.createCell(4);
        celluleValeur.setCellValue(facture.getTotal());
    }

    public void classeurClient(Workbook workbook, Client client) {
        feuilleClient(workbook, client);

        for (Facture facture : client.getFactures()) {
            feuilleFacture(workbook, facture);
        }
    }
}
