package com.bymdev.artem.ecommercedemo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.util.List;

@Document(indexName = "order_index")
@Setting(settingPath = "/elasticsearch-settings.json")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDoc {

    @Id
    private String id;
    @Field(type = FieldType.Text, analyzer = "product_name_analyzer")
    private String productName;
    private List<Integer> orderIdList;
}
