/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service.db1;

import com.advantech.model.db1.LineUserReference;
import com.advantech.dao.db1.LineUserReferenceDAO;
import com.advantech.model.db1.Line;
import java.util.List;
import java.util.Objects;
import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class LineUserReferenceService {

    @Autowired
    private LineUserReferenceDAO dao;

    public List<LineUserReference> findAll() {
        return dao.findAll();
    }

    public LineUserReference findByPrimaryKey(Object obj_id) {
        return dao.findByPrimaryKey(obj_id);
    }

    public List<LineUserReference> findByLine(Line line) {
        return dao.findByLine(line);
    }

    public List<LineUserReference> findByLines(List<Line> lines) {
        return dao.findByLines(lines);
    }

    public List<LineUserReference> findByLineAndDate(Line line, DateTime d) {
        return dao.findByLineAndDate(line, d);
    }

    public List<LineUserReference> findByLinesAndDate(List<Line> line, DateTime d) {
        return dao.findByLinesAndDate(line, d);
    }

    public int insert(LineUserReference pojo) {
        return dao.insert(pojo);
    }

    public int update(LineUserReference pojo) {
        return dao.update(pojo);
    }

    public int updateStationPeople(LineUserReference pojo) {
        List<LineUserReference> existSettings = this.findByLine(pojo.getLine());

        LineUserReference userSetting = existSettings.stream()
                .filter(s -> Objects.equals(s.getUser().getId(), pojo.getUser().getId()))
                .findFirst()
                .orElse(null);

        LineUserReference currentSetting = existSettings.stream()
                .filter(s -> s.getStation() == pojo.getStation())
                .findFirst()
                .orElse(null);

        if (userSetting != null) {
            dao.delete(currentSetting);
            dao.delete(userSetting);

            int id1 = currentSetting.getId();
            int id2 = userSetting.getId();

            currentSetting.setId(id2);
            userSetting.setId(id1);

            dao.insert(currentSetting);
            dao.insert(userSetting);
        } else {
            dao.delete(currentSetting);
            dao.insert(pojo);
        }
        return 1;
    }

    public int delete(LineUserReference pojo) {
        return dao.delete(pojo);
    }

}
