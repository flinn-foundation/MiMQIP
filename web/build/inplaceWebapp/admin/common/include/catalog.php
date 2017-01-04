                Search the Catalog:<br/>
                <form name="search" id="search" action="search.php">
                <input type="text" name="keys" />
                <a href="javascript:document.forms['search'].submit();">Search</a><br/><br/>
                </form>
                Browse the Catalog:<br/><br/>
<?php 
  while ($cat = get_all_categories("c.priority, c.name", "l.level=1")) {
    if ($cat['numproducts'] > 0) 
    echo "<a href='category.php?id=".$cat['id']."'>".$cat['name']."</a><br/>\n";
    while ($subcat = get_all_categories("c.priority, c.name", "l.level=2", 2)) {
      if ($subcat['numproducts'] > 0) 
      echo "&nbsp;&nbsp;&nbsp;<a href='category.php?id=".$subcat['id']."'>".$subcat['name']."</a><br/>\n";
      while ($subsubcat = get_all_categories("c.priority, c.name", "l.level=3", 3)) {
        if ($subsubcat['numproducts'] > 0) 
        echo "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='category.php?id=".$subsubcat['id']."'>".$subsubcat['name']."</a><br/>\n";
      }
    }
  }
?>
