package com.autism_rehablito_app.service.impl;

import com.autism_rehablito_app.entity.Parent;
import com.autism_rehablito_app.entity.Payment;
import com.autism_rehablito_app.repository.ParentRepository;
import com.autism_rehablito_app.repository.PaymentRepository;
import com.autism_rehablito_app.service.ParentPaymentReceiptService;
import com.autism_rehablito_app.exception.ResourceNotFoundException;
import com.autism_rehablito_app.exception.UnauthorizedException;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class ParentPaymentReceiptServiceImpl
        implements ParentPaymentReceiptService {

    private final ParentRepository parentRepository;
    private final PaymentRepository paymentRepository;

    public ParentPaymentReceiptServiceImpl(
            ParentRepository parentRepository,
            PaymentRepository paymentRepository) {

        this.parentRepository = parentRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] generateReceipt(Long userId, Long paymentId) {

        Parent parent = parentRepository.findByUserId(userId)
                .filter(Parent::getActive)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Active parent profile not found"));

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found"));

        if (!payment.getParent().getId().equals(parent.getId())) {
            throw new UnauthorizedException(
                    "You are not authorized to view this payment");
        }

        try {
            return createPdf(payment);
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Unable to generate payment receipt");
        }
    }

    private byte[] createPdf(Payment payment) throws Exception {

        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();

        Document document = new Document(PageSize.A4);

        PdfWriter.getInstance(document, outputStream);

        document.open();

        Font titleFont = new Font(
                Font.HELVETICA,
                20,
                Font.BOLD
        );

        Font headingFont = new Font(
                Font.HELVETICA,
                14,
                Font.BOLD
        );

        Font normalFont = new Font(
                Font.HELVETICA,
                11,
                Font.NORMAL
        );

        Paragraph title = new Paragraph(
                "AUTISM CENTRE",
                titleFont
        );

        title.setAlignment(Element.ALIGN_CENTER);

        document.add(title);

        Paragraph receiptTitle = new Paragraph(
                "Payment Receipt",
                headingFont
        );

        receiptTitle.setAlignment(Element.ALIGN_CENTER);

        document.add(receiptTitle);

        document.add(new Paragraph(" "));

        PdfPTable paymentTable = new PdfPTable(2);

        paymentTable.setWidthPercentage(100);

        addRow(
                paymentTable,
                "Receipt Reference",
                payment.getReceiptReference()
        );

        addRow(
                paymentTable,
                "Payment ID",
                String.valueOf(payment.getId())
        );

        addRow(
                paymentTable,
                "Child Name",
                payment.getChild().getChildName()
        );

        addRow(
                paymentTable,
                "Therapy",
                payment.getTherapyType()
        );

        addRow(
                paymentTable,
                "Actual Fee",
                formatAmount(payment.getActualFee())
        );

        addRow(
                paymentTable,
                "Discount",
                formatAmount(payment.getDiscount())
        );

        addRow(
                paymentTable,
                "Final Payable",
                formatAmount(payment.getFinalPayable())
        );

        addRow(
                paymentTable,
                "Paid Amount",
                formatAmount(payment.getPaidAmount())
        );

        addRow(
                paymentTable,
                "Currency",
                payment.getCurrency()
        );

        addRow(
                paymentTable,
                "Payment Status",
                payment.getStatus().name()
        );

        String paymentDate = payment.getPaymentDate() == null
                ? "-"
                : payment.getPaymentDate()
                        .plusHours(5)
                        .plusMinutes(30)
                        .format(DateTimeFormatter.ofPattern(
                                "dd-MM-yyyy"
                        ));
        addRow(
                paymentTable,
                "Payment Date",
                paymentDate
        );

        document.add(paymentTable);

        document.add(new Paragraph(" "));

        Paragraph note = new Paragraph(
                "Thank you for your payment.",
                normalFont
        );

        note.setAlignment(Element.ALIGN_CENTER);

        document.add(note);

        document.close();

        return outputStream.toByteArray();
    }

    private void addRow(
            PdfPTable table,
            String label,
            String value) {

        PdfPCell labelCell =
                new PdfPCell(new Phrase(label));

        PdfPCell valueCell =
                new PdfPCell(new Phrase(value));

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private String formatAmount(BigDecimal amount) {

        if (amount == null) {
            return "0.00";
        }

        return "₹" + amount.setScale(
                2,
                java.math.RoundingMode.HALF_UP
        );
    }
}