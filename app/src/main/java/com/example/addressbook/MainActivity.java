package com.example.addressbook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 刘杰
 */
public class MainActivity extends AppCompatActivity {
    Button buttonAdd ;
    ListView listViewPhone;
    private List<Phone> phones= new ArrayList<>();
    ListAdapter adapter;
    SQLiteDatabase db;
    Datebase dbHelper = new Datebase(this,"PhoneNumber",null,1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonAdd = findViewById(R.id.button_add);
        listViewPhone = findViewById(R.id.list_list);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddPhoneNumber.class);
                startActivity(intent);
            }});
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("PhoneNumber", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String phone1 = cursor.getString(cursor.getColumnIndex("phone1"));
                String phone2 = cursor.getString(cursor.getColumnIndex("phone2"));
                String housePhone = cursor.getString(cursor.getColumnIndex("housePhone"));
                String officePone = cursor.getString(cursor.getColumnIndex("officePhone"));
                String address = cursor.getString(cursor.getColumnIndex("address"));
                String remark = cursor.getString(cursor.getColumnIndex("remark"));
                Phone phoneInfo = new Phone(name, phone1, phone2, housePhone, officePone, address, remark);
                phones.add(phoneInfo);
            } while (cursor.moveToNext());
        }
        adapter = new ListAdapter(MainActivity.this, R.layout.list_item, phones);
        listViewPhone.setAdapter(adapter);
        listViewPhone.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                deleteDialog(i);
                return true;
            }
        });
        adapter.setOnItemCallClickListener(new ListAdapter.OnItemCallListener() {
            @Override
            public void onCallClick(int i) {
                Phone phoneCheck = phones.get(i);
                String phoneNumber = phoneCheck.getPhone1();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
            }
        });
        adapter.setOnItemChangesClickListener(new ListAdapter.OnItemChangesListener() {
            @Override
            public void onChangesClick(int i) {
                Phone phoneCheck = phones.get(i);
                String checkName = phoneCheck.getName(),
                        checkPhone1 = phoneCheck.getPhone1(),
                        checkPhone2 = phoneCheck.getPhone2(),
                        checkHousePhone = phoneCheck.getHouerPhone(),
                        checkOfficePhone = phoneCheck.getOfficephone(),
                        checkAddress = phoneCheck.getAddress(),
                        checkRemark = phoneCheck.getRemark();
                Intent intent = new Intent(MainActivity.this, EditPhone.class);
                intent.putExtra("extra_name", checkName);
                intent.putExtra("extra_phone1", checkPhone1);
                intent.putExtra("extra_phone2", checkPhone2);
                intent.putExtra("extra_housePhone", checkHousePhone);
                intent.putExtra("extra_officePhone", checkOfficePhone);
                intent.putExtra("extra_address", checkAddress);
                intent.putExtra("extra_remark", checkRemark);
                intent.putExtra("extra_i", i);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "编辑", Toast.LENGTH_SHORT).show();
            }});
        adapter.setOnItemMassgasClickListener(new ListAdapter.OnItemMassgasListener() {
            @Override
            public void onMassgasClick(int i) {
                Phone phoneCheck = phones.get(i);
                String phoneNumber = phoneCheck.getPhone1();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("smsto:" + phoneNumber));
                startActivity(intent);
                Toast.makeText(MainActivity.this, "短信", Toast.LENGTH_SHORT).show();
            }});}
    private void deleteDialog(final int positon){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("删除联系人");
        builder.setTitle("提示");
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Phone phoneCheck = phones.get(positon);
                String checkName = phoneCheck.getName(),
                        checkPhone1 = phoneCheck.getPhone1(),
                        checkPhone2 = phoneCheck.getPhone2(),
                        checkHousePhone = phoneCheck.getHouerPhone(),
                        checkOfficePhone = phoneCheck.getOfficephone(),
                        checkAddress = phoneCheck.getAddress(),
                        checkRemark = phoneCheck.getRemark();
                phones.remove(positon);
                adapter.notifyDataSetChanged();  //更新listView
                db.delete("PhoneNumber","name = ? and phone1 = ? and phone2 = ? and housePhone = ? and officePhone = ? and address = ? and remark = ?",new String[]{checkName,checkPhone1,checkPhone2,checkHousePhone,checkOfficePhone,checkAddress,checkRemark});
                Toast.makeText(MainActivity.this,"删除成功！",Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }
}
