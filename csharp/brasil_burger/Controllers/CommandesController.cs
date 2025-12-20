using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using brasil_burger.Models;
using brasil_burger.Data;
using System.Text.Json;

namespace brasil_burger.Controllers
{
    public class CommandesController : Controller
    {
        private readonly BrasilBurgerContext _context;

        public CommandesController(BrasilBurgerContext context)
        {
            _context = context;
        }

        [HttpPost]
        public async Task<IActionResult> PlaceOrder(string orderType, string paymentMethod)
        {
           
            var clientId = HttpContext.Session.GetInt32("ClientId");
            if (clientId == null)
            {
                
                return RedirectToAction("Login", "Account");
            }

            
            var cartJson = HttpContext.Session.GetString("Cart");
            if (string.IsNullOrEmpty(cartJson)) 
                return RedirectToAction("Index", "Catalogue");

            var cartItems = JsonSerializer.Deserialize<List<CartItem>>(cartJson);
            
           
            var total = cartItems != null ? cartItems.Sum(i => i.Total) : 0m;
            
            var nouvelleCommande = new Commande
            {
                IdClient = clientId.Value, 
                ModeConsommation = orderType switch {
                    "Sur place" => "SUR_PLACE",
                    "À emporter" => "A_RECUPERER",
                    "Livraison" => "A_LIVRER",
                    _ => "SUR_PLACE"
                },
                MontantTotal = total,
                Statut = "EN_COURS", 
                DateCommande = DateTime.UtcNow 
            };

            _context.Commandes.Add(nouvelleCommande);
            await _context.SaveChangesAsync();

           
            var nouveauPaiement = new Paiement
            {
                IdCommande = nouvelleCommande.Id, 
                Montant = total,
                ModePaiement = paymentMethod == "Orange Money" ? "OM" : "Wave",
                DatePaiement = DateTime.UtcNow,
            };

            _context.Paiements.Add(nouveauPaiement);
            await _context.SaveChangesAsync();

           
            HttpContext.Session.Remove("Cart");

            return View("Success", nouvelleCommande);
        }

      
        public IActionResult Success(Commande commande)
        {
            return View(commande);
        }

       
        public async Task<IActionResult> Historique()
        {
            var clientId = HttpContext.Session.GetInt32("ClientId");
            if (clientId == null) return RedirectToAction("Login", "Account");

            var commandes = await _context.Commandes
                .Where(c => c.IdClient == clientId.Value) 
                .OrderByDescending(c => c.DateCommande)
                .ToListAsync();

            return View(commandes);
        }
    }
}