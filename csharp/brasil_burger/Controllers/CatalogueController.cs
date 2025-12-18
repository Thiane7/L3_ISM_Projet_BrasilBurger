using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using brasil_burger.Data;
using brasil_burger.Models;

namespace brasil_burger.Controllers;

public class CatalogueController : Controller {
    private readonly BrasilBurgerContext _context;

    public CatalogueController(BrasilBurgerContext context) {
        _context = context;
    }

    public async Task<IActionResult> Index() {
        var viewModel = new CatalogueViewModel {
            Burgers = await _context.Burgers.Where(b => !b.EstArchive).ToListAsync(),
            Menus = await _context.Menus.Where(m => !m.EstArchive).ToListAsync(),
            Complements = await _context.Complements.Where(c => !c.EstArchive).ToListAsync()
        };
        return View(viewModel);
    }

    public async Task<IActionResult> Details(int id, string type) {
        if (type == "Burger") {
            var burger = await _context.Burgers.FindAsync(id);
            // On récupère les compléments pour les proposer lors de la commande
            ViewBag.Complements = await _context.Complements.Where(c => !c.EstArchive).ToListAsync();
            return View("DetailsBurger", burger);
        }
        // Logique similaire pour Menu...
        return RedirectToAction("Index");
    }
}