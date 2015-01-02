package dmillerw.agile.serialize.thread;

import com.google.common.collect.ImmutableList;
import dmillerw.agile.management.Ownership;
import dmillerw.agile.serialize.Serializer;

public class ThreadSaveOwnership extends Thread {

    private ImmutableList<String> nickList;
    private ImmutableList<String> maskList;

    public ThreadSaveOwnership() {
        nickList = ImmutableList.copyOf(Ownership.allowedUsers);
        maskList = ImmutableList.copyOf(Ownership.allowedMasks);
    }

    @Override
    public void run() {
        Serializer.saveOwnership(nickList, maskList);
    }
}
