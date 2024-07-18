/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fineract.portfolio.loanaccount.data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.fineract.infrastructure.core.service.DateUtils;
import org.apache.fineract.infrastructure.core.service.MathUtil;
import org.apache.fineract.organisation.monetary.data.CurrencyData;
import org.apache.fineract.portfolio.loanaccount.domain.LoanTransactionType;
import org.apache.fineract.portfolio.loanaccount.loanschedule.data.LoanScheduleData;
import org.apache.fineract.portfolio.loanaccount.loanschedule.data.LoanSchedulePeriodData;
import org.springframework.util.CollectionUtils;

/**
 * Immutable data object representing loan summary information.
 */
@Data
@Builder
@Accessors(chain = true)
public class LoanSummaryData {

    private final CurrencyData currency;
    private final BigDecimal principalDisbursed;
    private final BigDecimal principalAdjustments;
    private final BigDecimal principalPaid;
    private final BigDecimal principalWrittenOff;
    private final BigDecimal principalOutstanding;
    private final BigDecimal principalOverdue;
    private final BigDecimal interestCharged;
    private final BigDecimal interestPaid;
    private final BigDecimal interestWaived;
    private final BigDecimal interestWrittenOff;
    private final BigDecimal interestOutstanding;
    private final BigDecimal interestOverdue;
    private final BigDecimal feeChargesCharged;
    private final BigDecimal feeAdjustments;
    private final BigDecimal feeChargesDueAtDisbursementCharged;
    private final BigDecimal feeChargesPaid;
    private final BigDecimal feeChargesWaived;
    private final BigDecimal feeChargesWrittenOff;
    private final BigDecimal feeChargesOutstanding;
    private final BigDecimal feeChargesOverdue;
    private final BigDecimal penaltyChargesCharged;
    private final BigDecimal penaltyAdjustments;
    private final BigDecimal penaltyChargesPaid;
    private final BigDecimal penaltyChargesWaived;
    private final BigDecimal penaltyChargesWrittenOff;
    private final BigDecimal penaltyChargesOutstanding;
    private final BigDecimal penaltyChargesOverdue;
    private final BigDecimal totalExpectedRepayment;
    private final BigDecimal totalRepayment;
    private final BigDecimal totalExpectedCostOfLoan;
    private final BigDecimal totalCostOfLoan;
    private final BigDecimal totalWaived;
    private final BigDecimal totalWrittenOff;
    private final BigDecimal totalOutstanding;
    private final BigDecimal totalOverdue;
    private final BigDecimal totalRecovered;
    private final LocalDate overdueSinceDate;
    private final Long writeoffReasonId;
    private final String writeoffReason;

    // Adding fields for transaction summary
    private BigDecimal totalMerchantRefund;
    private BigDecimal totalMerchantRefundReversed;
    private BigDecimal totalPayoutRefund;
    private BigDecimal totalPayoutRefundReversed;
    private BigDecimal totalGoodwillCredit;
    private BigDecimal totalGoodwillCreditReversed;
    private BigDecimal totalChargeAdjustment;
    private BigDecimal totalChargeAdjustmentReversed;
    private BigDecimal totalChargeback;
    private BigDecimal totalCreditBalanceRefund;
    private BigDecimal totalCreditBalanceRefundReversed;
    private BigDecimal totalRepaymentTransaction;
    private BigDecimal totalRepaymentTransactionReversed;
    private BigDecimal totalInterestPaymentWaiver;
    private final Long chargeOffReasonId;
    private final String chargeOffReason;

    private BigDecimal totalUnpaidAccruedDueInterest;
    private BigDecimal totalUnpaidAccruedNotDueInterest;

    public static LoanSummaryData withTransactionAmountsSummary(final LoanSummaryData defaultSummaryData,
            final Collection<LoanTransactionData> loanTransactions, final LoanScheduleData repaymentSchedule) {

        BigDecimal totalMerchantRefund = BigDecimal.ZERO;
        BigDecimal totalMerchantRefundReversed = BigDecimal.ZERO;
        BigDecimal totalPayoutRefund = BigDecimal.ZERO;
        BigDecimal totalPayoutRefundReversed = BigDecimal.ZERO;
        BigDecimal totalGoodwillCredit = BigDecimal.ZERO;
        BigDecimal totalGoodwillCreditReversed = BigDecimal.ZERO;
        BigDecimal totalChargeAdjustment = BigDecimal.ZERO;
        BigDecimal totalChargeAdjustmentReversed = BigDecimal.ZERO;
        BigDecimal totalChargeback = BigDecimal.ZERO;
        BigDecimal totalCreditBalanceRefund = BigDecimal.ZERO;
        BigDecimal totalCreditBalanceRefundReversed = BigDecimal.ZERO;
        BigDecimal totalRepaymentTransaction = BigDecimal.ZERO;
        BigDecimal totalRepaymentTransactionReversed = BigDecimal.ZERO;
        BigDecimal totalInterestPaymentWaiver = BigDecimal.ZERO;
        BigDecimal totalUnpaidAccruedDueInterest = BigDecimal.ZERO;
        BigDecimal totalUnpaidAccruedNotDueInterest = BigDecimal.ZERO;

        if (!CollectionUtils.isEmpty(loanTransactions)) {

            totalMerchantRefund = computeTotalAmountForNonReversedTransactions(LoanTransactionType.MERCHANT_ISSUED_REFUND,
                    loanTransactions);
            totalMerchantRefundReversed = computeTotalAmountForReversedTransactions(LoanTransactionType.MERCHANT_ISSUED_REFUND,
                    loanTransactions);
            totalPayoutRefund = computeTotalAmountForNonReversedTransactions(LoanTransactionType.PAYOUT_REFUND, loanTransactions);
            totalPayoutRefundReversed = computeTotalAmountForReversedTransactions(LoanTransactionType.PAYOUT_REFUND, loanTransactions);
            totalGoodwillCredit = computeTotalAmountForNonReversedTransactions(LoanTransactionType.GOODWILL_CREDIT, loanTransactions);
            totalGoodwillCreditReversed = computeTotalAmountForReversedTransactions(LoanTransactionType.GOODWILL_CREDIT, loanTransactions);
            totalChargeAdjustment = computeTotalAmountForNonReversedTransactions(LoanTransactionType.CHARGE_ADJUSTMENT, loanTransactions);
            totalChargeAdjustmentReversed = computeTotalAmountForReversedTransactions(LoanTransactionType.CHARGE_ADJUSTMENT,
                    loanTransactions);
            totalChargeback = computeTotalAmountForNonReversedTransactions(LoanTransactionType.CHARGEBACK, loanTransactions);
            totalCreditBalanceRefund = computeTotalAmountForNonReversedTransactions(LoanTransactionType.CREDIT_BALANCE_REFUND,
                    loanTransactions);
            totalCreditBalanceRefundReversed = computeTotalAmountForReversedTransactions(LoanTransactionType.CREDIT_BALANCE_REFUND,
                    loanTransactions);
            totalRepaymentTransaction = computeTotalRepaymentTransactionAmount(loanTransactions);
            totalRepaymentTransactionReversed = computeTotalAmountForReversedTransactions(LoanTransactionType.REPAYMENT, loanTransactions);
            totalInterestPaymentWaiver = computeTotalAmountForNonReversedTransactions(LoanTransactionType.INTEREST_PAYMENT_WAIVER,
                    loanTransactions);
        }

        if (repaymentSchedule != null) {
            // Accrued Due Interest on Past due installments
            totalUnpaidAccruedDueInterest = computeTotalAccruedDueInterestAmount(repaymentSchedule.getPeriods());
            if (MathUtil.isGreaterThanZero(totalUnpaidAccruedDueInterest)) {
                totalUnpaidAccruedDueInterest = totalUnpaidAccruedDueInterest
                        .subtract(computeTotalInterestPaidDueAmount(repaymentSchedule.getPeriods()));
            }

            // Accrued Due Interest on Actual Installment
            totalUnpaidAccruedNotDueInterest = computeTotalAccruedNotDueInterestAmountOnActualPeriod(repaymentSchedule.getPeriods());
            if (MathUtil.isGreaterThanZero(totalUnpaidAccruedNotDueInterest)) {
                totalUnpaidAccruedNotDueInterest = totalUnpaidAccruedNotDueInterest
                        .subtract(computeTotalInterestPaidNotDueAmountOnActualPeriod(repaymentSchedule.getPeriods()));
            }
        }

        return LoanSummaryData.builder().currency(defaultSummaryData.currency).principalDisbursed(defaultSummaryData.principalDisbursed)
                .principalAdjustments(defaultSummaryData.principalAdjustments).principalPaid(defaultSummaryData.principalPaid)
                .principalWrittenOff(defaultSummaryData.principalWrittenOff).principalOutstanding(defaultSummaryData.principalOutstanding)
                .principalOverdue(defaultSummaryData.principalOverdue).interestCharged(defaultSummaryData.interestCharged)
                .interestPaid(defaultSummaryData.interestPaid).interestWaived(defaultSummaryData.interestWaived)
                .interestWrittenOff(defaultSummaryData.interestWrittenOff).interestOutstanding(defaultSummaryData.interestOutstanding)
                .interestOverdue(defaultSummaryData.interestOverdue).feeChargesCharged(defaultSummaryData.feeChargesCharged)
                .feeAdjustments(defaultSummaryData.feeAdjustments)
                .feeChargesDueAtDisbursementCharged(defaultSummaryData.feeChargesDueAtDisbursementCharged)
                .feeChargesPaid(defaultSummaryData.feeChargesPaid).feeChargesWaived(defaultSummaryData.feeChargesWaived)
                .feeChargesWrittenOff(defaultSummaryData.feeChargesWrittenOff)
                .feeChargesOutstanding(defaultSummaryData.feeChargesOutstanding).feeChargesOverdue(defaultSummaryData.feeChargesOverdue)
                .penaltyChargesCharged(defaultSummaryData.penaltyChargesCharged).penaltyAdjustments(defaultSummaryData.penaltyAdjustments)
                .penaltyChargesPaid(defaultSummaryData.penaltyChargesPaid).penaltyChargesWaived(defaultSummaryData.penaltyChargesWaived)
                .penaltyChargesWrittenOff(defaultSummaryData.penaltyChargesWrittenOff)
                .penaltyChargesOutstanding(defaultSummaryData.penaltyChargesOutstanding)
                .penaltyChargesOverdue(defaultSummaryData.penaltyChargesOverdue)
                .totalExpectedRepayment(defaultSummaryData.totalExpectedRepayment).totalRepayment(defaultSummaryData.totalRepayment)
                .totalExpectedCostOfLoan(defaultSummaryData.totalExpectedCostOfLoan).totalCostOfLoan(defaultSummaryData.totalCostOfLoan)
                .totalWaived(defaultSummaryData.totalWaived).totalWrittenOff(defaultSummaryData.totalWrittenOff)
                .totalOutstanding(defaultSummaryData.totalOutstanding).totalOverdue(defaultSummaryData.totalOverdue)
                .overdueSinceDate(defaultSummaryData.overdueSinceDate).writeoffReasonId(defaultSummaryData.writeoffReasonId)
                .writeoffReason(defaultSummaryData.writeoffReason).totalRecovered(defaultSummaryData.totalRecovered)
                .chargeOffReasonId(defaultSummaryData.chargeOffReasonId).chargeOffReason(defaultSummaryData.chargeOffReason)
                .totalMerchantRefund(totalMerchantRefund).totalMerchantRefundReversed(totalMerchantRefundReversed)
                .totalPayoutRefund(totalPayoutRefund).totalPayoutRefundReversed(totalPayoutRefundReversed)
                .totalGoodwillCredit(totalGoodwillCredit).totalGoodwillCreditReversed(totalGoodwillCreditReversed)
                .totalChargeAdjustment(totalChargeAdjustment).totalChargeAdjustmentReversed(totalChargeAdjustmentReversed)
                .totalChargeback(totalChargeback).totalCreditBalanceRefund(totalCreditBalanceRefund)
                .totalCreditBalanceRefundReversed(totalCreditBalanceRefundReversed).totalRepaymentTransaction(totalRepaymentTransaction)
                .totalRepaymentTransactionReversed(totalRepaymentTransactionReversed).totalInterestPaymentWaiver(totalInterestPaymentWaiver)
                .totalUnpaidAccruedDueInterest(totalUnpaidAccruedDueInterest)
                .totalUnpaidAccruedNotDueInterest(totalUnpaidAccruedNotDueInterest).build();
    }

    public static LoanSummaryData withOnlyCurrencyData(CurrencyData currencyData) {
        return LoanSummaryData.builder().currency(currencyData).build();
    }

    private static BigDecimal computeTotalAmountForReversedTransactions(LoanTransactionType transactionType,
            Collection<LoanTransactionData> loanTransactions) {
        return loanTransactions.stream().filter(
                transaction -> transaction.getType().getCode().equals(transactionType.getCode()) && transaction.getReversedOnDate() != null)
                .map(txn -> txn.getAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal computeTotalAmountForNonReversedTransactions(LoanTransactionType transactionType,
            Collection<LoanTransactionData> loanTransactions) {
        return loanTransactions.stream().filter(
                transaction -> transaction.getType().getCode().equals(transactionType.getCode()) && transaction.getReversedOnDate() == null)
                .map(txn -> txn.getAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal computeTotalRepaymentTransactionAmount(Collection<LoanTransactionData> loanTransactions) {
        BigDecimal totalRepaymentTransaction = computeTotalAmountForNonReversedTransactions(LoanTransactionType.REPAYMENT,
                loanTransactions);
        BigDecimal totalDownPaymentTransaction = computeTotalAmountForNonReversedTransactions(LoanTransactionType.DOWN_PAYMENT,
                loanTransactions);
        return totalRepaymentTransaction.add(totalDownPaymentTransaction);
    }

    private static BigDecimal computeTotalAccruedDueInterestAmount(Collection<LoanSchedulePeriodData> periods) {
        final LocalDate businessDate = DateUtils.getBusinessLocalDate();
        return periods.stream().filter(period -> !period.getDownPaymentPeriod() && businessDate.isAfter(period.getDueDate()))
                .map(period -> period.getTotalAccruedInterest()).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal computeTotalInterestPaidDueAmount(Collection<LoanSchedulePeriodData> periods) {
        final LocalDate businessDate = DateUtils.getBusinessLocalDate();
        return periods.stream().filter(period -> !period.getDownPaymentPeriod() && businessDate.isAfter(period.getDueDate()))
                .map(period -> period.getInterestPaid()).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal computeTotalAccruedNotDueInterestAmountOnActualPeriod(Collection<LoanSchedulePeriodData> periods) {
        final LocalDate businessDate = DateUtils.getBusinessLocalDate();
        return periods.stream()
                .filter(period -> !period.getDownPaymentPeriod() && isActualPeriod(period) && businessDate.isBefore(period.getDueDate()))
                .map(period -> period.getTotalAccruedInterest()).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal computeTotalInterestPaidNotDueAmountOnActualPeriod(Collection<LoanSchedulePeriodData> periods) {
        final LocalDate businessDate = DateUtils.getBusinessLocalDate();
        return periods.stream()
                .filter(period -> !period.getDownPaymentPeriod() && isActualPeriod(period) && businessDate.isBefore(period.getDueDate()))
                .map(period -> period.getInterestPaid()).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static boolean isActualPeriod(LoanSchedulePeriodData period) {
        final LocalDate businessDate = DateUtils.getBusinessLocalDate();
        boolean actualPeriod = false;
        if (period.getPeriod() != null) {
            if (period.getPeriod() == 1) {
                actualPeriod = ((businessDate.isEqual(period.getFromDate()) || businessDate.isAfter(period.getFromDate()))
                        && businessDate.isBefore(period.getDueDate()));
            } else {
                actualPeriod = (businessDate.isAfter(period.getFromDate()) && businessDate.isBefore(period.getDueDate()));
            }
        }

        return actualPeriod;
    }
}
