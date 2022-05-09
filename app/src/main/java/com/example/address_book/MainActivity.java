package com.example.address_book;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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
                Intent intent = new Intent(MainActivity.this,AddPhoneNumber.class);
                startActivity(intent);
            }
        });

        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("PhoneNumber",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do{
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String phone1 = cursor.getString(cursor.getColumnIndex("phone1"));
                String phone2 = cursor.getString(cursor.getColumnIndex("phone2"));
                String housePhone = cursor.getString(cursor.getColumnIndex("housePhone"));
                String officePone = cursor.getString(cursor.getColumnIndex("officePhone"));
                String address = cursor.getString(cursor.getColumnIndex("address"));
                String remark = cursor.getString(cursor.getColumnIndex("remark"));
                Phone phoneInfo = new Phone(name,phone1,phone2,housePhone,officePone,address,remark);
                phones.add(phoneInfo);
            }while (cursor.moveToNext());
        }
        adapter = new ListAdapter(MainActivity.this,R.layout.list_item, phones);
        listViewPhone.setAdapter(adapter);


        listViewPhone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Phone phone_check = phones.get(i);
                String checkName = phone_check.getName(),
                        checkPhone1 = phone_check.getPhone1(),
                        checkPhone2 = phone_check.getPhone2(),
                        checkHousePhone = phone_check.getHouerPhone(),
                        checkOfficePhone = phone_check.getOfficephone(),
                        checkAddress = phone_check.getAddress(),
                        checkRemark = phone_check.getRemark();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setMessage(
                        "         姓               名：" + checkName + "\n" +
                                "         联 系 方 式 1 ：" + checkPhone1 + "\n" +
                                "         联 系 方 式 2 ：" + checkPhone2 + "\n" +
                                "         家 庭 座 机 号：" + checkHousePhone + "\n" +
                                "         办 公 座 机 号：" + checkOfficePhone + "\n" +
                                "         地               址 ：" + checkAddress + "\n" +
                                "         备               注 ：" + checkRemark + "\n");
                builder.setTitle("                  查看联系人");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
//                Intent intent = new Intent(MainActivity.this,EditPhone.class);
//                intent.putExtra("extra_name",checkName);
//                intent.putExtra("extra_phone1",checkPhone1);
//                intent.putExtra("extra_phone2",checkPhone2);
//                intent.putExtra("extra_housePhone",checkHousePhone);
//                intent.putExtra("extra_officePhone",checkOfficePhone);
//                intent.putExtra("extra_address",checkAddress);
//                intent.putExtra("extra_remark",checkRemark);
//                startActivity(intent);
            }
        });
        listViewPhone.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                DeleteDialog(i);
                return true;
            }
        });

        adapter.setOnItemCallClickListener(new ListAdapter.onItemCallListener() {
            @Override
            public void onCallClick(int i) {

                Phone phone_check = phones.get(i);
                String phoneNumber = phone_check.getPhone1();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
            }
        });
        adapter.setOnItemChangesClickListener(new ListAdapter.onItemChangesListener() {
            @Override
            public void onChangesClick(int i) {
                Phone phone_check = phones.get(i);
                String checkName = phone_check.getName(),
                        checkPhone1 = phone_check.getPhone1(),
                        checkPhone2 = phone_check.getPhone2(),
                        checkHousePhone = phone_check.getHouerPhone(),
                        checkOfficePhone = phone_check.getOfficephone(),
                        checkAddress = phone_check.getAddress(),
                        checkRemark = phone_check.getRemark();
                Intent intent = new Intent(MainActivity.this, EditPhone.class);
                intent.putExtra("extra_name",checkName);
                intent.putExtra("extra_phone1",checkPhone1);
                intent.putExtra("extra_phone2",checkPhone2);
                intent.putExtra("extra_housePhone",checkHousePhone);
                intent.putExtra("extra_officePhone",checkOfficePhone);
                intent.putExtra("extra_address",checkAddress);
                intent.putExtra("extra_remark",checkRemark);
                intent.putExtra("extra_i",i);
                startActivity(intent);
                Toast.makeText(MainActivity.this,"编辑",Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setOnItemMassgasClickListener(new ListAdapter.onItemMassgasListener() {
            @Override
            public void onMassgasClick(int i) {
                Phone phone_check = phones.get(i);
                String phoneNumber = phone_check.getPhone1();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("smsto:" + phoneNumber));
                startActivity(intent);
                Toast.makeText(MainActivity.this,"短信",Toast.LENGTH_SHORT).show();
            }
        });



    }
    private void DeleteDialog(final int positon){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("删除联系人");
        builder.setTitle("提示");
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Phone phone_check = phones.get(positon);
                String checkName = phone_check.getName(),
                        checkPhone1 = phone_check.getPhone1(),
                        checkPhone2 = phone_check.getPhone2(),
                        checkHousePhone = phone_check.getHouerPhone(),
                        checkOfficePhone = phone_check.getOfficephone(),
                        checkAddress = phone_check.getAddress(),
                        checkRemark = phone_check.getRemark();
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
