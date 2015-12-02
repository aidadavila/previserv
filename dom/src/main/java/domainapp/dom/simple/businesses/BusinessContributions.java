package domainapp.dom.simple.businesses;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.googlecode.wickedcharts.highcharts.options.*;
import com.googlecode.wickedcharts.highcharts.options.Title;
import com.googlecode.wickedcharts.highcharts.options.color.HexColor;
import com.googlecode.wickedcharts.highcharts.options.color.HighchartsColor;
import com.googlecode.wickedcharts.highcharts.options.color.NullColor;
import com.googlecode.wickedcharts.highcharts.options.color.RadialGradient;
import com.googlecode.wickedcharts.highcharts.options.functions.PercentageFormatter;
import com.googlecode.wickedcharts.highcharts.options.series.Point;
import com.googlecode.wickedcharts.highcharts.options.series.PointSeries;
import com.googlecode.wickedcharts.highcharts.options.series.Series;
import domainapp.dom.simple.items.Item;
import domainapp.dom.simple.items.ItemCategory;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.query.QueryDefault;
import org.isisaddons.wicket.wickedcharts.cpt.applib.WickedChart;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 10/27/2015.
 */

@DomainService(nature = NatureOfService.VIEW_CONTRIBUTIONS_ONLY)
@DomainObjectLayout(cssClassFa = "fa-bar-chart")

public class BusinessContributions {

    //region > TransactionsByBusinessChart
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(named = "Transactions by Location Chart",
            contributed = Contributed.AS_ACTION
    )
    @MemberOrder(sequence = "1", name = "businessTransactions")
    public WickedChart TransactionsByBusinessChart(Business business) {

        Map<BusinessLocation, AtomicInteger> byBusinessLocation = Maps.newTreeMap(); //each element of the chart will be a BusinessLocation
        List<BusinessTransaction> listAllBusinessTransactions = businessTransactions.businessTransactionsByBusiness(business);
        for (BusinessTransaction businessTransaction : listAllBusinessTransactions) {
            BusinessLocation businessLocation = businessTransaction.getBusinessLocation();
            AtomicInteger integer = byBusinessLocation.get(businessLocation);
            if(integer == null) {
                integer = new AtomicInteger();
                byBusinessLocation.put(businessLocation, integer);
            }
            //integer.incrementAndGet(); // (use this line if you want to graph the count of transactions )
            integer.addAndGet(businessTransaction.getAmount().intValue()); // (use this line if you want to graph the amount value of transactions )
        }

        return new WickedChart(new PieWithGradientOptions(byBusinessLocation));
    }
    //endregion

    public static class PieWithGradientOptions extends Options {
        private static final long serialVersionUID = 1L;

        public PieWithGradientOptions(Map<BusinessLocation, AtomicInteger> byBusinessLocation) {

            setChartOptions(new ChartOptions()
                    .setPlotBackgroundColor(new NullColor())
                    .setPlotBorderWidth(null)
                    .setPlotShadow(Boolean.FALSE));

            setTitle(new Title("Transactions by Location"));

            PercentageFormatter formatter = new PercentageFormatter();
            setTooltip(
                    new Tooltip()
                            .setFormatter(
                                    formatter)
                            .       setPercentageDecimals(1));

            setPlotOptions(new PlotOptionsChoice()
                    .setPie(new PlotOptions()
                            .setAllowPointSelect(Boolean.TRUE)
                            .setCursor(Cursor.POINTER)
                            .setDataLabels(new DataLabels()
                                    .setEnabled(Boolean.TRUE)
                                    .setColor(new HexColor("#000000"))
                                    .setConnectorColor(new HexColor("#000000"))
                                    .setFormatter(formatter))));

            Series<Point> series = new PointSeries()
                    .setType(SeriesType.BAR);
            int i=0;
            for (Map.Entry<BusinessLocation, AtomicInteger> entry : byBusinessLocation.entrySet()) {
                series
                        .addPoint(
                                new Point(entry.getKey().getName(), entry.getValue().get()).setColor(
                                        new RadialGradient()
                                                .setCx(0.5)
                                                .setCy(0.3)
                                                .setR(0.7)
                                                .addStop(0, new HighchartsColor(i))
                                                .addStop(1, new HighchartsColor(i).brighten(-0.3f))));
                i++;
            }
            addSeries(series);
        }
    }

    //region > listAllItemCategories (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            named = "List All Item Categories",
            contributed = Contributed.AS_ACTION
    )
    @MemberOrder(sequence = "2")
    public List<ItemCategory> listAllItemCategories(Business business){
        return container.allMatches(ItemCategory.class, new Predicate<ItemCategory>() {
            @Override public boolean apply(final ItemCategory input) {
                return input.getBusiness().equals(business);
            }
        });
    }

    //endregion

    //region > findItemCategoriesById (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            named = "Find Item Categories by Id"
    )
    @MemberOrder(sequence = "3", name = "itemCategories")
    public List<ItemCategory> findItemCategoriesById(
            @ParameterLayout(named="Business") final Business parBusiness,
            @ParameterLayout(named="Item Category Id") final String itemCategoryId

    ) {
        return container.allMatches(
                new QueryDefault<>(
                        ItemCategory.class,
                        "findItemCategoryPerBusinessById",
                        "itemCategoryId", itemCategoryId, "parBusiness", parBusiness ));
    }
    //endregion

    //region > findItemCategoriesByName (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            named = "Find Item Categories by Name"
    )
    @MemberOrder(sequence = "4", name = "itemCategories")
    public List<ItemCategory> findItemCategoriesByName(
            @ParameterLayout(named="Business") final Business parBusiness,
            @ParameterLayout(named="Name") final String name
    ) {
        return container.allMatches(
                new QueryDefault<>(
                        ItemCategory.class,
                        "findItemCategoriesPerBusinessByName","name", name, "parBusiness", parBusiness));
    }
    //endregion

    //region > listAllItems (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            contributed = Contributed.AS_ACTION,
            named = "List All Items"
    )
    @MemberOrder(sequence = "5",name = "items")
    public List<Item> listAllItems(Business business){
        return container.allMatches(Item.class, new Predicate<Item>() {
            @Override public boolean apply(final Item input) {
                return input.getBusiness().equals(business);
            }
        });
    }

    //endregion

    //region > findItemsById (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            contributed = Contributed.AS_ACTION,
            named = "Find Items by Id"
    )
    @MemberOrder(sequence = "6",name = "items")
    public List<Item> findItemsById(
            @ParameterLayout(named="Business") final Business parBusiness,
            @ParameterLayout(named="Item Id") final String itemId
    ) {
        return container.allMatches(
                new QueryDefault<>(
                        Item.class,
                        "findItemPerBusinessById",
                        "itemId", itemId, "parBusiness", parBusiness));
    }
    //endregion

    //region > findItemsPerBusinessByName (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            contributed = Contributed.AS_ACTION,
            named = "Find Items by Name"
    )
    @MemberOrder(sequence = "7",name = "items")
    public List<Item> findItemsPerBusinessByName(
            @ParameterLayout(named="Business") final Business parBusiness,
            @ParameterLayout(named="Name") final String name
    ) {
        return container.allMatches(
                new QueryDefault<>(
                        Item.class,
                        "findItemsPerBusinessByName",
                        "name", name, "parBusiness", parBusiness));
    }
    //endregion



    //region Injected Services

    @Inject
    private BusinessTransactionRepository businessTransactions;
    private Business business;

    @Inject
    DomainObjectContainer container;

    //endregion

}
