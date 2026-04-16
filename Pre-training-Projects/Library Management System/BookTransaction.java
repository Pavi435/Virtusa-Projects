import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BookTransaction {
    private int loanId;
    private int bkId;
    private int usrId;
    private String issueDateStr;

    public BookTransaction(int loanId, int bkId, int usrId, String issueDateStr) {
        this.loanId = loanId;
        this.bkId = bkId;
        this.usrId = usrId;
        this.issueDateStr = issueDateStr;
    }

    public int getBkId() { return bkId; }
    public String getIssueDateStr() { return issueDateStr; }

    public long calcLateFee(String returnDateStr) {
        LocalDate iDate = LocalDate.parse(this.issueDateStr);
        LocalDate rDate = LocalDate.parse(returnDateStr);

        long totalDays = ChronoUnit.DAYS.between(iDate, rDate);
        long allowedDays = 7;

        if (totalDays > allowedDays) {
            long extraDays = totalDays - allowedDays;
            return extraDays * 5;
        }
        return 0;
    }

    @Override
    public String toString() {
        return loanId + "," + bkId + "," + usrId + "," + issueDateStr;
    }
}