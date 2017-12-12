package com.zemian.adocblog.service;

import com.zemian.adocblog.BaseSpringTest;
import com.zemian.adocblog.data.dao.Paging;
import com.zemian.adocblog.data.dao.PagingList;
import com.zemian.adocblog.data.domain.Setting;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;

@ContextConfiguration(classes = ServiceConfig.class)
public class SettingServiceTest extends BaseSpringTest {
    @Autowired
    private SettingService settingService;

    @Autowired
    private JdbcTemplate jdbc;

    @Test
    public void crud() {
        Setting test = new Setting();
        try {
            test.setCategory("TEST");
            test.setName("DataConfigTest");
            test.setValue("Foo");
            test.setType(Setting.Type.STRING);

            settingService.create(test);
            assertThat(test.getSettingId(), greaterThanOrEqualTo(1));

            Setting test2 = settingService.get(test.getSettingId());
            assertThat(test2.getSettingId(), is(test.getSettingId()));
            assertThat(test2.getCategory(), is("TEST"));
            assertThat(test2.getName(), is("DataConfigTest"));
            assertThat(test2.getValue(), is("Foo"));

            List<Setting> list = settingService.findAll(new Paging()).getList();
            assertThat(list.size(), greaterThanOrEqualTo(1));
        } finally {
            settingService.delete(test.getSettingId());
            try {
                settingService.get(test.getSettingId());
                Assert.fail("There should not be record id=" + test.getSettingId() + " after delete.");
            } catch (RuntimeException e) {
                // Expected.
            }
        }
    }

    @Test
    public void category() {
        List<Setting> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String group = (i % 2 == 0) ? "A" : "B";
            Setting test = new Setting();
            test.setCategory("CATEGORY_TEST_" + group);
            test.setName("key" + i);
            test.setValue("" + i);
            test.setType(Setting.Type.INTEGER);

            settingService.create(test);
            list.add(test);
        }

        try {
            // Find by category.
            Map<String, Setting> list2 = settingService.getCategoryMap("CATEGORY_TEST_A");
            assertThat(list2.size(), is(5));
            list2.entrySet().stream().forEach(e -> {
                String name = e.getKey();
                Setting setting = e.getValue();
                assertThat(name, startsWith("key"));
                assertThat(setting.getCategory(), is("CATEGORY_TEST_A"));
                assertThat(setting.getName(), is(name));
                Integer n = setting.getValueByType();
                assertThat(n % 2, is(0));
            });

            list2 = settingService.getCategoryMap("CATEGORY_TEST_B");
            list2.entrySet().stream().forEach(e -> {
                String name = e.getKey();
                Setting setting = e.getValue();
                assertThat(name, startsWith("key"));
                assertThat(setting.getCategory(), is("CATEGORY_TEST_B"));
                assertThat(setting.getName(), is(name));
                Integer n = setting.getValueByType();
                assertThat(n % 2, is(1));
            });
            assertThat(list2.size(), is(5));
        } finally {
            for (Setting setting : list) {
                settingService.delete(setting.getSettingId());
            }
        }
    }

    @Test
    public void paging() {
        List<Setting> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Setting test = new Setting();
            test.setCategory("PAGING_TEST");
            test.setName("key" + i);
            test.setValue("Foo" + i);
            test.setType(Setting.Type.STRING);

            settingService.create(test);
            list.add(test);
        }

        try {
            Paging paging = new Paging(0, 25);
            PagingList<Setting> plist = settingService.findAll(paging);
            assertThat(plist.getList().size(), is(25));
            assertThat(plist.isMore(), is(true));
            assertThat(plist.getPaging().getOffset(), is(paging.getOffset()));
            assertThat(plist.getPaging().getSize(), is(paging.getSize()));

            int totalCount = jdbc.queryForObject("SELECT COUNT(*) FROM settings", Integer.class);
            paging = new Paging(0, totalCount + 1);
            plist = settingService.findAll(paging);
            assertThat(plist.getList().size(), is(totalCount));
            assertThat(plist.isMore(), is(false));

            paging = new Paging(25, 25);
            PagingList<Setting> plist2 = settingService.findAll(paging);
            assertThat(plist2.getList().size(), is(25));
            assertThat(plist2.isMore(), is(true));
            assertThat(plist2.getList().get(0).getSettingId(), not(plist.getList().get(0).getSettingId()));

            paging = new Paging(totalCount - 25, 25);
            plist = settingService.findAll(paging);
            assertThat(plist.getList().size(), is(25));
            assertThat(plist.isMore(), is(false));

            // Test query by category group
            List<Setting> catList = settingService.findByCategory("PAGING_TEST");
            assertThat(catList.size(), is(100));
            assertThat(catList.stream().allMatch(s -> s.getCategory().equals("PAGING_TEST")), is(true));
        } finally {
            for (Setting setting : list) {
                settingService.delete(setting.getSettingId());
            }
        }
    }
}