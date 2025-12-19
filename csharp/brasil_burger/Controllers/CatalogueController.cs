using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using brasil_burger.Data;
using brasil_burger.Models;

namespace brasil_burger.Controllers;

public class CatalogueController : Controller
{
    private readonly BrasilBurgerContext _context;

    public CatalogueController(BrasilBurgerContext context)
    {
        _context = context;
    }

    public async Task<IActionResult> Index(string? filter)
    {
        filter ??= "Tous";
        ViewBag.SelectedFilter = filter;

        var viewModel = new CatalogueViewModel();

        if (filter.Equals("Tous", StringComparison.OrdinalIgnoreCase))
        {
            viewModel.Burgers = await _context.Burgers.Where(b => !b.EstArchive).ToListAsync();
            viewModel.Menus = await _context.Menus.Where(m => !m.EstArchive).ToListAsync();
            viewModel.Complements = await _context.Complements.Where(c => !c.EstArchive).ToListAsync();
        }
        else if (filter.Equals("Burgers", StringComparison.OrdinalIgnoreCase))
        {
            viewModel.Burgers = await _context.Burgers.Where(b => !b.EstArchive).ToListAsync();
        }
        else if (filter.Equals("Menus", StringComparison.OrdinalIgnoreCase))
        {
            viewModel.Menus = await _context.Menus.Where(m => !m.EstArchive).ToListAsync();
        }
        else if (filter.Equals("Complements", StringComparison.OrdinalIgnoreCase) || filter.Equals("Compléments", StringComparison.OrdinalIgnoreCase))
        {
            viewModel.Complements = await _context.Complements.Where(c => !c.EstArchive).ToListAsync();
        }

        return View(viewModel);
    }

    public async Task<IActionResult> Details(int id, string type)
    {
        if (type == "Burger")
        {
            var burger = await _context.Burgers.FindAsync(id);

            ViewBag.Complements = await _context.Complements.Where(c => !c.EstArchive).ToListAsync();
            return View("DetailsBurger", burger);
        }

        return RedirectToAction("Index");
    }
}