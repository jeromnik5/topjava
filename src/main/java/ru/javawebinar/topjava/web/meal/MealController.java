package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;



/**
 * Created by jerom on 16/02/17.
 */
@Controller
@RequestMapping(value = "/meals")
public class MealController extends MealRestController {
    private int getId(HttpServletRequest request) {
        return  Integer.valueOf(request.getParameter("id"));
    }

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public String create(Model model) {
        model.addAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),"", 1000));
        return "meal";
    }

    @RequestMapping(value="/update", method = RequestMethod.GET)
    public String update(Model model, HttpServletRequest request) {
        model.addAttribute("meal", super.get(getId(request)));
        return "meal";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(HttpServletRequest request) {
        super.delete(getId(request));
        return "redirect:meals";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String updateOrCreate(HttpServletRequest request) {
        Meal meal = new Meal(String.valueOf(getId(request)).isEmpty() ? null : getId(request),
                LocalDateTime.parse(request.getParameter("dateTime")), request.getParameter("description"),
                Integer.valueOf(request.getParameter("calories")));
        if(meal.isNew()) {
            super.create(meal);
        } else {
            super.update(meal, meal.getId());
        }
        return "redirect:meals";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.POST)
        public String getBetween(HttpServletRequest request, Model model ) {
        LocalDate start = DateTimeUtil.parseLocalDate(request.getParameter("startDate"));
        LocalDate end = DateTimeUtil.parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = DateTimeUtil.parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = DateTimeUtil.parseLocalTime(request.getParameter("endTime"));
        model.addAttribute("meals", super.getBetween(start, startTime, end, endTime));
        return "meals";
    }
  }


