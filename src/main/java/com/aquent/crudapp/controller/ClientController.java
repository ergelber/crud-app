package com.aquent.crudapp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.aquent.crudapp.domain.Client;
import com.aquent.crudapp.service.ClientService;

/**
 * Controller for handling basic Client management operations.
 */
@Controller
@RequestMapping("client")
public class ClientController {

    public static final String COMMAND_DELETE = "Delete";

    @Inject private ClientService ClientService;

    /**
     * Renders the listing page.
     *
     * @return list view populated with the current list of people
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView mav = new ModelAndView("client/listclient");
        mav.addObject("clients", ClientService.listEntities());
        return mav;
    }

    /**
     * Renders an empty form used to create a new Client record.
     *
     * @return create view populated with an empty Client
     */
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public ModelAndView create() {
        ModelAndView mav = new ModelAndView("client/createclient");
        mav.addObject("client", new Client());
        mav.addObject("errors", new ArrayList<String>());
        return mav;
    }

    /**
     * Validates and saves a new Client.
     * On success, the user is redirected to the listing page.
     * On failure, the form is redisplayed with the validation errors.
     *
     * @param Client populated form bean for the Client
     * @return redirect, or create view with errors
     */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public ModelAndView create(Client Client) {
        List<String> errors = ClientService.validateEntity(Client);
        if (errors.isEmpty()) {
            ClientService.createEntity(Client);
            return new ModelAndView("redirect:/client/listclient");
        } else {
            ModelAndView mav = new ModelAndView("client/createclient");
            mav.addObject("client", Client);
            mav.addObject("errors", errors);
            return mav;
        }
    }

    /**
     * Renders an edit form for an existing Client record.
     *
     * @param ClientId the ID of the Client to edit
     * @return edit view populated from the Client record
     */
    @RequestMapping(value = "edit/{clientId}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable Integer ClientId) {
        ModelAndView mav = new ModelAndView("client/editclient");
        mav.addObject("client", ClientService.readEntity(ClientId));
        mav.addObject("errors", new ArrayList<String>());
        return mav;
    }

    /**
     * Validates and saves an edited Client.
     * On success, the user is redirected to the listing page.
     * On failure, the form is redisplayed with the validation errors.
     *
     * @param Client populated form bean for the Client
     * @return redirect, or edit view with errors
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public ModelAndView edit(Client Client) {
        List<String> errors = ClientService.validateEntity(Client);
        if (errors.isEmpty()) {
            ClientService.updateEntity(Client);
            return new ModelAndView("redirect:/client/listclient");
        } else {
            ModelAndView mav = new ModelAndView("client/editclient");
            mav.addObject("client", Client);
            mav.addObject("errors", errors);
            return mav;
        }
    }

    /**
     * Renders the deletion confirmation page.
     *
     * @param ClientId the ID of the Client to be deleted
     * @return delete view populated from the Client record
     */
    @RequestMapping(value = "delete/{clientId}", method = RequestMethod.GET)
    public ModelAndView delete(@PathVariable Integer clientId) {
        ModelAndView mav = new ModelAndView("client/deleteclient");
        mav.addObject("client", ClientService.readEntity(clientId));
        return mav;
    }

    /**
     * Handles Client deletion or cancellation, redirecting to the listing page in either case.
     *
     * @param command the command field from the form
     * @param ClientId the ID of the Client to be deleted
     * @return redirect to the listing page
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String delete(@RequestParam String command, @RequestParam Integer clientId) {
        if (COMMAND_DELETE.equals(command)) {
            ClientService.deleteEntity(clientId);
        }
        return "redirect:/client/listclient";
    }
}

