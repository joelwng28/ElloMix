//
//  LogInViewController.swift
//  Ellomix
//
//  Created by Micah Peoples on 1/30/17.
//  Copyright © 2017 micah. All rights reserved.
//

import UIKit
import AFNetworking


class LogInViewController: UIViewController {
    @IBOutlet weak var facebookButton: UIButton!
    @IBOutlet weak var facebookIcon: UIImageView!
    @IBOutlet weak var logo: UIImageView!

    override func viewDidLoad() {
        super.viewDidLoad()
        
        
        // Do any additional setup after loading the view.
        self.view.backgroundColor = UIColor(red:0.15, green:0.15, blue:0.15, alpha:1.0)
        
        let facebookIconPath = "facebook_logowhite.png"
        facebookIcon.image = UIImage(named: facebookIconPath)
        
        let ellomixPath = "ellomix.png"
        logo.image = UIImage(named: ellomixPath)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
