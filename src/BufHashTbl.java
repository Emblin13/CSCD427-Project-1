import java.util.ArrayList;
import java.util.List;

public class BufHashTbl {
    private final List<List<BufTblRecord>> records; //Forced to make double list of lists because java doesn't support arrays of generics for some dumb reason
    private final int tableSize = 10;

    public BufHashTbl() {
        records = new ArrayList<>();
        for (int i = 0; i < tableSize; i++) {
            records.add(new ArrayList<>());
        }
    }

    public void insert(int pageNum, int frameNum) {
        BufTblRecord newRecord = new BufTblRecord(pageNum, frameNum);
        records.get(pageNum % tableSize).add(newRecord);
    }


    public int lookup(int pageNum) {
        for (BufTblRecord record : records.get(pageNum % tableSize)) {
            if (record.pageNum == pageNum) {
                return record.frameNum;
            }
        }
        return -1; //framenum not found
    }

    public boolean remove(int pageNum, int frameNum) {
        for (BufTblRecord record : records.get(pageNum % tableSize)) {
            if (record.pageNum == pageNum && record.frameNum == frameNum) {
                records.get(pageNum % tableSize).remove(record);
                return true;
            }
        }
        return true; // you need to change the returned value
    }

    private static class BufTblRecord {
        public int pageNum;
        public int frameNum;

        public BufTblRecord (int pageNum, int frameNum) {
            this.pageNum = pageNum;
            this.frameNum = frameNum;
        }
    }
}
