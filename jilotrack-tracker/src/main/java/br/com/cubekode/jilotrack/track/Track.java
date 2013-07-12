package br.com.cubekode.jilotrack.track;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import br.com.cubekode.jilotrack.util.IOUtil;

public class Track implements Externalizable {

    private String tracking;

    private Integer index;

    private String key;

    private int deep = 0;

    private long beginTime = -1;

    private long endTime = -1;

    private Integer parent;

    private List<Integer> children;

    private String data;

    private String error;

    public Track(String tracking, Integer index, String key) {
        this(tracking, index, key, null);
    }

    public Track(String tracking, Integer index, String key, Track parent) {
        this.tracking = tracking;
        this.index = index;
        this.key = key;
        if (parent != null) {
            this.parent = parent.index;
            this.deep = parent.deep + 1;
            parent.child(index);
        }
    }

    protected Track child(Integer child) {
        if (children == null) {
            children = new LinkedList<Integer>();
        }
        children.add(child);
        return this;
    }

    public Track begin() {
        if (beginTime == -1) {
            beginTime = System.currentTimeMillis();
            // log(">" + this);
        }
        return this;
    }

    public Track end() {
        if (endTime == -1) {
            endTime = System.currentTimeMillis();
            // log("<" + this);
        }
        return this;
    }

    public String getTracking() {
        return tracking;
    }

    public long getTime() {
        if (endTime >= 0 && beginTime >= 0) {
            return endTime - beginTime;
        }
        return -1;
    }

    public boolean isRunning() {
        return beginTime != -1 && endTime == -1;
    }

    public Integer getIndex() {
        return index;
    }

    public String getKey() {
        return key;
    }

    public int getDeep() {
        return deep;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public Integer getParent() {
        return parent;
    }

    public List<Integer> getChildren() {
        if (children == null) {
            return Collections.emptyList();
        }
        return children;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public void writeExternal(ObjectOutput in) throws IOException {

        in.writeInt(deep);
        in.writeLong(beginTime);
        in.writeLong(endTime);

        IOUtil.writeFieldString(in, tracking);
        IOUtil.writeFieldString(in, key);
        IOUtil.writeFieldInteger(in, index);
        IOUtil.writeFieldString(in, data);
        IOUtil.writeFieldString(in, error);
        IOUtil.writeFieldInteger(in, parent);

        if (IOUtil.writeField(in, children)) {
            in.writeInt(children.size());
            for (Integer i : children) {
                in.writeInt(i);
            }
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

        deep = in.readInt();
        beginTime = in.readLong();
        endTime = in.readLong();

        tracking = IOUtil.readFieldString(in);
        key = IOUtil.readFieldString(in);
        index = IOUtil.readFieldInteger(in);
        data = IOUtil.readFieldString(in);
        error = IOUtil.readFieldString(in);
        parent = IOUtil.readFieldInteger(in);

        if (IOUtil.readField(in)) {
            int size = in.readInt();
            children = new ArrayList<Integer>(size + 1);
            for (int i = 0; i < size; i++) {
                children.add(in.readInt());
            }
        } else {
            children = null;
        }
    }

    @Override
    public int hashCode() {
        return index.hashCode();
    }

    @Override
    public String toString() {
        return "Track " + "[ tracking=" + tracking + ", index=" + index + ", key=" + key + ", deep=" + deep + ", running="
            + isRunning() + ", time=" + getTime() + ", beginTime=" + beginTime + ", endTime=" + endTime + ", parent=" + parent
            + ", children=" + (children != null ? children.size() : 0)
            + ", data=" + (data != null) + ", error=" + (error != null) + "]";
    }

    void log(String m) {
        final String tabs = "------------------------------------------------------------------------------";
        System.out.print(tabs.substring(0, Math.min(tabs.length(), deep)));
        System.out.println(m);
    }
}
