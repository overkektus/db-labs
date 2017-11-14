import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import java.util.ArrayList;

import category.Category;
import ped.bstu.by.calendartasks.R;

/**
 * Created by Egor on 20.10.2017.
 */

public class TaskAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater inflater;
    ArrayList<Category> objects;

    TaskAdapter(Context context, ArrayList<Category> categories) {
        ctx = context;
        objects = categories;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        /*
        if (view == null) {
            view = inflater.inflate(R.layout.item, parent, false);
        }

        Category c = getCategory(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        ((TextView) view.findViewById(R.id.cat)).setText(c.getCategory());

        CheckBox cbBuy = (CheckBox) view.findViewById(R.id.cbBox);
        // присваиваем чекбоксу обработчик
        cbBuy.setOnCheckedChangeListener(myCheckChangeList);
        // пишем позицию
        cbBuy.setTag(position);
        // заполняем данными из товаров: в корзине или нет
        cbBuy.setChecked(p.box);
        */
        return view;
    }

    Category getCategory(int position) {
        return ((Category) getItem(position));
    }

    OnCheckedChangeListener myCheckChangeList = new OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            // меняем данные товара (в корзине или нет)
            //getProduct((Integer) buttonView.getTag()).box = isChecked;
        }
    };

}
