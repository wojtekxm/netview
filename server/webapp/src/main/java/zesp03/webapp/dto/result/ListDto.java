/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.webapp.dto.result;


import java.time.Instant;
import java.util.List;
import java.util.function.Supplier;

public class ListDto<T> extends BaseResultDto {
    private List<T> list;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public static <T> ListDto<T> make(Supplier<List<T>> supplier) {
        final Instant t0 = Instant.now();
        final ListDto<T> result = new ListDto<>();
        final List<T> list = supplier.get();
        result.setList(list);
        result.makeQueryTime(t0);
        return result;
    }
}
