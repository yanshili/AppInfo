package me.mooney.lib.listview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 所有listView的适配器基类
 * Created by yanshili on 2016/6/23.
 */
public abstract class BASEAdapter<T> extends BaseAdapter {


    protected String TAG=getClass().getSimpleName();

    protected Context context;
    protected LayoutInflater inflater;
    protected List<T> itemList = new ArrayList<T>();

    public BASEAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    /**
     * 判断数据是否为空
     *
     * @return 为空返回true，不为空返回false
     */
    public boolean isEmpty() {
        return itemList.isEmpty();
    }

    /**
     * 在原有的数据上添加新数据
     *
     * @param item
     */
    public void addItem(T item) {
        if(this.itemList== null) {
            this.itemList = new ArrayList<>();
        }
        this.itemList.add(item);
        notifyDataSetChanged();
    }

    /**
     * 在原有的数据上添加新数据
     *
     * @param itemList
     */
    public void addItems(List<T> itemList) {
        if(this.itemList== null) {
            this.itemList = new ArrayList<>();
        }
        this.itemList.addAll(itemList);
        notifyDataSetChanged();
    }

    /**
     * 设置为新的数据，旧数据会被清空
     *
     * @param itemList
     */
    public void setItems(List<T> itemList) {
        this.itemList = itemList;
        if(this.itemList== null) {
            this.itemList = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    /**
     * 清空数据
     */
    public void clearItems() {
        if (itemList!=null){
            itemList=new ArrayList<>();
            notifyDataSetChanged();
        }
    }

    /**
     * 更新可见视图
     * @param listView
     */
    public void notifyVisibleItemView(ListView listView) {

        int startShownIndex=listView.getFirstVisiblePosition();
        int endShownIndex=listView.getLastVisiblePosition();

        for (int i=startShownIndex;i<endShownIndex;i++){

            View view = listView.getChildAt(i);

            getView(i, view, listView);
        }
    }


    /**
     * 更新指定位置视图
     * @param listView
     * @param targetIndex
     */
    public void notifyItemView(ListView listView, int targetIndex) {

        int startShownIndex=listView.getFirstVisiblePosition();
        int endShownIndex=listView.getLastVisiblePosition();

        if (targetIndex >= startShownIndex
                && targetIndex <= endShownIndex) {
            View view = listView.getChildAt(targetIndex - startShownIndex);

            getView(targetIndex, view, listView);
        }
    }

    @Override
    public int getCount() {
        int count=0;
        if (itemList!=null){
            count=itemList.size();
        }
        return count;
    }

    @Override
    public T getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    abstract public View getView(int position, View convertView, ViewGroup parent);

    /**
     * 对指定图片着色
     * @param drawableId    图片id
     * @param colorId       颜色id
     * @return              返回着色后的图片
     */
    protected Drawable tintDrawableColor(@DrawableRes int drawableId, @ColorRes int colorId){

        Drawable drawable= ContextCompat.getDrawable(context,drawableId);
        Drawable drawable1= DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable1, ContextCompat.getColor(context,colorId));

        return drawable1;
    }

    /**
     * 图片着色选择器
     * @param drawableResId     图片id
     * @param colorSelectorId   颜色选择id
     * @return
     */
    protected Drawable tintDrawableColorList(@DrawableRes int drawableResId, @ColorRes int colorSelectorId){
        ColorStateList colorStateList= ContextCompat.getColorStateList(context,colorSelectorId);
        Drawable drawable= ContextCompat.getDrawable(context,drawableResId).mutate();
        Drawable drawable1= DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(drawable1,colorStateList);
        return drawable1;
    }

}
