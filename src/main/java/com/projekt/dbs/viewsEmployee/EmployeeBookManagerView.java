package com.projekt.dbs.viewsEmployee;

import com.projekt.dbs.entities.Book;
import com.projekt.dbs.layoutComponents.BookForm;
import com.projekt.dbs.layoutComponents.EmployeeNavbar;
import com.projekt.dbs.service.EntityManagementService;
import com.projekt.dbs.tools.BookFilter;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.function.Consumer;

@CssImport("EmployeeView.css")
@Route(value = "EmployeeViewBooks")
public class EmployeeBookManagerView extends VerticalLayout {

    private Grid<Book> grid = new Grid<>(Book.class, false);

    private BookForm bookForm;

    private Button bookAddButton = new Button();
    private HorizontalLayout bookManager;

    private EntityManagementService service;

    private EmployeeNavbar navbar = new EmployeeNavbar();

    private BookFilter bookFilter;

    @Autowired
    public EmployeeBookManagerView(EntityManagementService service) {
        this.service = service;
        bookForm = new BookForm(service);
        bookManager = new HorizontalLayout(grid, bookForm, bookAddButton);
        ListDataProvider<Book> dataProvider = getDataProvider();
        GridListDataView<Book> dataView = grid.setItems(dataProvider);
        bookFilter = new BookFilter(dataView);
        configEditor();
        configGrid();
        configContent();
        add(navbar, bookManager);
    }

    private void configEditor() {
        closeEditor();
        bookForm.setWidth("10em");
        bookForm.addListener(BookForm.SaveEvent.class, this::saveBook);
        bookForm.addListener(BookForm.DeleteEvent.class, this::deleteBook);
        bookForm.addListener(BookForm.CloseEvent.class, e -> closeEditor());
    }

    private void deleteBook(BookForm.DeleteEvent event) {
        service.removeBook(event.getBook());
        updateGrid();
    }

    private void addBook() {
        grid.asSingleSelect().clear();
        editBook(new Book());
        bookForm.getISBN().setReadOnly(false);
    }

    private void saveBook(BookForm.SaveEvent event) {
        service.addBook(event.getBook());
        updateGrid();
        closeEditor();
    }

    private void editBook(Book book) {
        if (book == null) {
            closeEditor();
        } else {
            bookForm.setBook(book);
            bookForm.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        bookForm.setVisible(false);
        removeClassName("editing");
    }


    private void configGrid() {
        grid.setHeightFull();
        grid.setColumnReorderingAllowed(false);
        grid.setColumns("name", "bookAuthor", "publisher", "yearOfPublishing", "pages", "availableCopies");
        grid.getColumnByKey("name").setHeader("Název knihy");
        grid.getColumnByKey("bookAuthor").setHeader("Autor");
        grid.getColumnByKey("publisher").setHeader("Nakladatelství");
        grid.getColumnByKey("yearOfPublishing").setHeader("Rok vydání");
        grid.getColumnByKey("pages").setHeader("Počet stran");
        grid.getColumnByKey("availableCopies").setHeader("Dostupných kopií");
        grid.getColumns().forEach(column -> grid.setClassName("columns"));
        grid.asSingleSelect().addValueChangeListener(e -> {
            editBook(e.getValue());
            bookForm.getISBN().setReadOnly(true);
        });
        grid.getStyle().set("height", "revert-layer");
        HeaderRow headerRow = grid.appendHeaderRow();
        headerRow.getCell(grid.getColumnByKey("name")).setComponent(createFilterHeader("Filtruj dle názvu", VaadinIcon.BOOK.create(), bookFilter::setName));
        headerRow.getCell(grid.getColumnByKey("bookAuthor")).setComponent(createFilterHeader("Filtruj dle autora", VaadinIcon.USER.create(), bookFilter::setAuthor));
        headerRow.getCell(grid.getColumnByKey("publisher")).setComponent(createFilterHeader("Filtruj dle nakladatelství", VaadinIcon.ARCHIVES.create(), bookFilter::setPublisher));
        headerRow.getCell(grid.getColumnByKey("yearOfPublishing")).setComponent(createFilterHeader("Filtruj dle roku vydání", VaadinIcon.CALENDAR_CLOCK.create(), bookFilter::setYearOfPublishing));
        headerRow.getCell(grid.getColumnByKey("pages")).setComponent(createFilterHeader("Filtruj dle počtu stran", VaadinIcon.OPEN_BOOK.create(), bookFilter::setPages));
        headerRow.getCell(grid.getColumnByKey("availableCopies")).setComponent(createFilterHeader("Filtruj dle dostupných kopií", VaadinIcon.COPY_O.create(), bookFilter::setAvailableCopies));


    }

    private void updateGrid() {
        ListDataProvider<Book> dataProvider = getDataProvider();
        grid.setItems(dataProvider);
/*        UI.getCurrent().getPage().reload();*/
    }

    private void configContent() {
        bookAddButton.setClassName("add-book-button");
        bookAddButton.setIcon(new Icon(VaadinIcon.PLUS));
        bookAddButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        bookAddButton.addClickListener(e -> addBook());
        bookManager.setFlexGrow(2, grid);
        bookManager.setFlexGrow(1, bookForm);
        bookManager.setSizeFull();
    }


    private static Component createFilterHeader(String labelText, Component icon, Consumer<String> filterChangeConsumer) {
        TextField textField = new TextField();
        textField.setSuffixComponent(icon);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.setClearButtonVisible(true);
        textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        textField.setPlaceholder(labelText);
        textField.setWidthFull();
        textField.getStyle().set("max-width", "100%");
        textField.addValueChangeListener(e -> filterChangeConsumer.accept(e.getValue().toLowerCase()));
        VerticalLayout layout = new VerticalLayout(textField);
        layout.getThemeList().clear();
        layout.getThemeList().add("spacing-xs");
        return layout;
    }

    private ListDataProvider<Book> getDataProvider(){
        if(service.findAllBooks() != null){
            ListDataProvider<Book> dataProvider = new ListDataProvider<>(service.findAllBooks());
            return dataProvider;
        } else {
            return new ListDataProvider<>(Collections.emptyList());
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if(VaadinSession.getCurrent().getAttribute("employee") == null){
            UI.getCurrent().navigate("EmployeeLogin");
        }
    }


}
