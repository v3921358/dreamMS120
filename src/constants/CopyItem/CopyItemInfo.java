/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package constants.CopyItem;

/**
 * @author PlayDK
 */
public class CopyItemInfo {

    public final int itemId; //道具的ID
    public final int chrId;
    public final String name; //角色名字
    public boolean first; //是否是第1次

    public CopyItemInfo(int itemId, int chrId, String name) {
        this.itemId = itemId;
        this.chrId = chrId;
        this.name = name;
        this.first = true;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean f) {
        this.first = f;
    }
}
