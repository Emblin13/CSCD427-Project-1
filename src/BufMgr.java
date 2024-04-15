import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BufMgr {
    private BufHashTbl bufTbl;
    private Frame[] pool;
    private int poolSize;
    private List<Integer> lruQueue;
    private int used = 0;

    public BufMgr(int poolSize) {
        bufTbl = new BufHashTbl();
        this.poolSize = poolSize;
        this.pool = new Frame[poolSize];
        lruQueue = new ArrayList<>();
    }

    //"I want page X"
    public void pin(int pageNum) {
        Integer frameNum = bufTbl.lookup(pageNum);
        if (frameNum == -1) {
            //implements LRU replacement policy
            if (used < poolSize) {
                for (int i = 0; i < poolSize; i++) {
                    if (pool[i] == null) {
                        pool[i] = new Frame("This is page " + pageNum + ".");
                        bufTbl.insert(pageNum, i);
                        lruQueue.add(i);
                        used++;
                        return;
                    }
                }
            } else {
                int frameToReplace = lruQueue.get(0);
                lruQueue.remove(0);
                pool[frameToReplace] = new Frame("This is page " + pageNum + ".");
                bufTbl.remove(pageNum, frameToReplace);
                bufTbl.insert(pageNum, frameToReplace);
                lruQueue.add(frameToReplace);
            }
        }
    }

    //"I am done with page X"
    public void unpin(int pageNum) {
        // your code goes here
    }

    public void createPage(int pageNum) {
        String name = getPageFileName(pageNum);
        String contents = "This is page " + pageNum + ".";
        FileWriter writer = null;

        try {
            writer = new FileWriter(name, false);
            writer.write(contents);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Something went wrong while creating the page");
        }
    }

    public void readPage(int pageNum) {
        // your code goes here
    }

    public void writePage(int pageNum) {
        // your code goes here
    }

    public void displayPage(int pageNum) {
        Integer frameNum = bufTbl.lookup(pageNum);
        if (frameNum == -1) throw new IllegalArgumentException("Cannot display page that is not in memory");

        pool[frameNum].displayPage();
    }

    public void updatePage(int pageNum, String toAppend) {
        Integer frameNum = bufTbl.lookup(pageNum);
        if (frameNum == -1) throw new IllegalArgumentException("Cannot update page that is not in memory");

        pool[frameNum].updatePage(toAppend);
    }

    private String getPageFileName(int pageNum) {
        return pageNum + ".txt";
    }
}
